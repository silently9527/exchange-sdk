package org.herman.future.impl.binance;

import org.herman.Constants;
import org.herman.future.*;
import org.herman.future.impl.WebsocketRequest;
import org.herman.future.impl.WebsocketRequestClient;
import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.enums.OrderSide;
import org.herman.future.model.event.*;
import org.herman.future.model.market.OrderBookEntry;
import org.herman.utils.InputChecker;
import org.herman.utils.JsonWrapper;
import org.herman.utils.JsonWrapperArray;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class BinanceWebsocketRequestClient implements WebsocketRequestClient {
    private final BinanceRestApiRequestClient restApiRequestClient;
    private final Set<String> listenerKeys = new CopyOnWriteArraySet<>();

    public BinanceWebsocketRequestClient(FutureSubscriptionOptions options) {
        FutureRestApiOptions requestOptions = new FutureRestApiOptions(Constants.Future.BINANCE_REST_API_BASE_URL, options.getApiKey(), options.getSecretKey());
        this.restApiRequestClient = new BinanceRestApiRequestClient(requestOptions);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            listenerKeys.forEach(key -> RestApiInvoker.callSync(restApiRequestClient.keepListenKey(key)));
        }, 10, 30, TimeUnit.MINUTES);
    }

    @Override
    public WebsocketRequest<AggregateTradeEvent> subscribeAggregateTradeEvent(String symbol,
                                                                              FutureSubscriptionListener<AggregateTradeEvent> subscriptionListener,
                                                                              FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<AggregateTradeEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Aggregate Trade for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.aggregateTradeChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            AggregateTradeEvent result = new AggregateTradeEvent();
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            result.setId(data.getLong("a"));
            result.setEventType(data.getString("e"));
            result.setEventTime(data.getLong("E"));
            result.setPrice(data.getBigDecimal("p"));
            result.setSymbol(data.getString("s"));
            result.setFirstId(data.getLong("f"));
            result.setQty(data.getBigDecimal("q"));
            result.setLastId(data.getLong("l"));
            result.setTime(data.getLong("T"));
            result.setSide(data.getBoolean("m") ? OrderSide.SELL : OrderSide.BUY);
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<MarkPriceEvent> subscribeMarkPriceEvent(String symbol,
                                                                    FutureSubscriptionListener<MarkPriceEvent> subscriptionListener,
                                                                    FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<MarkPriceEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Mark Price for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.markPriceChannel(symbol.toLowerCase()));

        request.jsonParser = (jsonWrapper) -> {
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            MarkPriceEvent result = new MarkPriceEvent();
            result.setEventType(data.getString("e"));
            result.setEventTime(data.getLong("E"));
            result.setSymbol(data.getString("s"));
            result.setMarkPrice(data.getBigDecimal("p"));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<CandlestickEvent> subscribeCandlestickEvent(String symbol, CandlestickInterval interval,
                                                                        FutureSubscriptionListener<CandlestickEvent> subscriptionListener,
                                                                        FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<CandlestickEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Candlestick for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.candlestickChannel(symbol.toLowerCase(), interval));

        request.jsonParser = (jsonWrapper) -> {
            CandlestickEvent result = new CandlestickEvent();
            JsonWrapper data = jsonWrapper.getJsonObject("data");

            result.setSymbol(data.getString("s"));
            result.setEventType(data.getString("e"));
            result.setEventTime(data.getLong("E"));

            JsonWrapper jsondata = data.getJsonObject("k");
            result.setOpenTime(jsondata.getLong("t"));
            result.setOpen(jsondata.getBigDecimal("o"));
            result.setClose(jsondata.getBigDecimal("c"));
            result.setHigh(jsondata.getBigDecimal("h"));
            result.setLow(jsondata.getBigDecimal("l"));
            result.setVolume(jsondata.getBigDecimal("v"));
            result.setQuoteAssetVolume(jsondata.getBigDecimal("q"));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<OrderBookEvent> subscribeBookDepthEvent(String symbol, Integer limit,
                                                                    FutureSubscriptionListener<OrderBookEvent> subscriptionListener,
                                                                    FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(limit, "limit")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<OrderBookEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Partial Book Depth for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.bookDepthChannel(symbol.toLowerCase(), limit));

        request.jsonParser = (jsonWrapper) -> {
            OrderBookEvent result = new OrderBookEvent();
            JsonWrapper data = jsonWrapper.getJsonObject("data");

            result.setEventType(data.getString("e"));
            result.setEventTime(data.getLong("E"));
            result.setTransactionTime(data.getLong("T"));
            result.setSymbol(data.getString("s"));
            result.setFirstUpdateId(data.getLong("U"));
            result.setLastUpdateId(data.getLong("u"));
            result.setLastUpdateIdInlastStream(data.getLong("pu"));

            List<OrderBookEntry> elementList = new LinkedList<>();
            JsonWrapperArray dataArray = data.getJsonArray("b");
            dataArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice(item.getBigDecimalAt(0));
                element.setQty(item.getBigDecimalAt(1));
                elementList.add(element);
            });
            result.setBids(elementList);

            List<OrderBookEntry> askList = new LinkedList<>();
            JsonWrapperArray askArray = data.getJsonArray("a");
            askArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice(item.getBigDecimalAt(0));
                element.setQty(item.getBigDecimalAt(1));
                askList.add(element);
            });
            result.setAsks(askList);

            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<SymbolMiniTickerEvent> subscribeSymbolTickerEvent(String symbol, FutureSubscriptionListener<SymbolMiniTickerEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<SymbolMiniTickerEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Individual Symbol Ticker for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.tickerChannel(symbol.toLowerCase()));

        request.jsonParser = (jsonWrapper) -> {
            SymbolMiniTickerEvent result = new SymbolMiniTickerEvent();
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            result.setEventType(data.getString("e"));
            result.setEventTime(data.getLong("E"));
            result.setSymbol(data.getString("s"));
            result.setPrice(data.getBigDecimal("c"));
            result.setVolume(data.getBigDecimal("Q"));
            result.setQuoteAssetVolume(data.getBigDecimal("q"));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<List<SymbolMiniTickerEvent>> subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolMiniTickerEvent>> subscriptionListener,
                                                                                 FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<List<SymbolMiniTickerEvent>> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***All Market Tickers";
        request.connectionHandler = (connection) -> connection.send(Channels.tickerChannel());

        request.jsonParser = (jsonWrapper) -> {
            List<SymbolMiniTickerEvent> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach(item -> {
                SymbolMiniTickerEvent element = new SymbolMiniTickerEvent();
                element.setEventTime(item.getLong("E"));
                element.setEventType(item.getString("e"));
                element.setPrice(item.getBigDecimal("c"));
                element.setSymbol(item.getString("s"));
                element.setVolume(item.getBigDecimal("Q"));
                element.setQuoteAssetVolume(item.getBigDecimal("q"));
                result.add(element);
            });
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<SymbolBookTickerEvent> subscribeSymbolBookTickerEvent(String symbol, FutureSubscriptionListener<SymbolBookTickerEvent> subscriptionListener,
                                                                                  FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<SymbolBookTickerEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Individual Symbol Book Ticker for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.bookTickerChannel(symbol.toLowerCase()));

        request.jsonParser = (jsonWrapper) -> {
            SymbolBookTickerEvent result = new SymbolBookTickerEvent();
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            result.setOrderBookUpdateId(data.getLong("u"));
            result.setSymbol(data.getString("s"));
            result.setBestBidPrice(data.getBigDecimal("b"));
            result.setBestBidQty(data.getBigDecimal("B"));
            result.setBestAskPrice(data.getBigDecimal("a"));
            result.setBestAskQty(data.getBigDecimal("A"));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<SymbolBookTickerEvent> subscribeAllBookTickerEvent(FutureSubscriptionListener<SymbolBookTickerEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<SymbolBookTickerEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***All Market Book Tickers***";
        request.connectionHandler = (connection) -> connection.send(Channels.bookTickerChannel());

        request.jsonParser = (jsonWrapper) -> {
            SymbolBookTickerEvent result = new SymbolBookTickerEvent();
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            result.setOrderBookUpdateId(data.getLong("u"));
            result.setSymbol(data.getString("s"));
            result.setBestBidPrice(data.getBigDecimal("b"));
            result.setBestBidQty(data.getBigDecimal("B"));
            result.setBestAskPrice(data.getBigDecimal("a"));
            result.setBestAskQty(data.getBigDecimal("A"));
            return result;
        };
        return request;
    }

    @Override
    public String listenerKey(FutureSubscriptionOptions options) {
        String listenerKey = RestApiInvoker.callSync(restApiRequestClient.startListenKey());
        listenerKeys.add(listenerKey);
        return listenerKey;
    }


}
