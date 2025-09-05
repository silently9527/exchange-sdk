package org.herman.future.impl.binance;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.herman.Constants;
import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.RestApiInvoker;
import org.herman.future.impl.WebSocketConnection;
import org.herman.future.impl.WebsocketRequest;
import org.herman.future.impl.WebsocketRequestClient;
import org.herman.future.model.enums.*;
import org.herman.future.model.event.*;
import org.herman.future.model.market.OrderBookEntry;
import org.herman.future.model.user.BalanceUpdateEvent;
import org.herman.future.model.user.OrderUpdateEvent;
import org.herman.future.model.user.PositionUpdateEvent;
import org.herman.utils.InputChecker;
import org.herman.utils.JsonWrapper;
import org.herman.utils.JsonWrapperArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BinanceWebsocketRequestClient implements WebsocketRequestClient {
    private static final Logger log = LoggerFactory.getLogger(BinanceWebsocketRequestClient.class);

    private final BinanceRestApiRequestClient restApiRequestClient;
    private final String privateToken;

    public BinanceWebsocketRequestClient(BinanceFutureSubscriptionOptions options) {
        this.restApiRequestClient = new BinanceRestApiRequestClient(Constants.Future.BINANCE_REST_API_BASE_URL, options.getApiKey(), options.getSecretKey());
        this.privateToken = RestApiInvoker.callSync(this.restApiRequestClient.startListenKey());
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            RestApiInvoker.callSync(restApiRequestClient.keepListenKey(privateToken));
        }, 10, 30, TimeUnit.MINUTES);
    }

    @Override
    public WebsocketRequest<JsonWrapper> unsubscribe(List<String> channels) {
        WebsocketRequest<JsonWrapper> request = new BinanceWebsocketRequest<>(json -> {}, e -> log.error("unsubscribe", e));
        request.id= DigestUtils.md5Hex("unsubscribe");
        request.name = "***unsubscribe " + StringUtils.join(channels, ",") + "***";
        request.channels = new ArrayList<>();
        request.channels.add("unsubscribe");
        request.connectionHandler = (connection) -> connection.send(Channels.unsubscribe(channels));
        request.jsonParser = (jsonWrapper) -> jsonWrapper;
        return request;
    }

    @Override
    public WebsocketRequest<AggregateTradeEvent> subscribeAggregateTradeEvent(String symbol,
                                                                              FutureSubscriptionListener<AggregateTradeEvent> subscriptionListener,
                                                                              FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<AggregateTradeEvent> request = new BinanceWebsocketRequest<>(subscriptionListener, errorHandler);
        request.id= DigestUtils.md5Hex(Channels.aggregateTradeChannel(symbol.toLowerCase()));
        request.name = "***Aggregate Trade for " + symbol + "***";
        request.channels = Channels.aggTradeParams(symbol.toLowerCase());
        request.connectionHandler = (connection) -> connection.send(Channels.aggregateTradeChannel(symbol.toLowerCase()));

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
        WebsocketRequest<MarkPriceEvent> request = new BinanceWebsocketRequest<>(subscriptionListener, errorHandler);
        request.id= DigestUtils.md5Hex(Channels.markPriceChannel(symbol.toLowerCase()));
        request.name = "***Mark Price for " + symbol + "***";
        request.channels = Channels.markPriceParams(symbol.toLowerCase());
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
        WebsocketRequest<CandlestickEvent> request = new BinanceWebsocketRequest<>(subscriptionListener, errorHandler);
        request.id= DigestUtils.md5Hex(Channels.candlestickChannel(symbol.toLowerCase(), interval));
        request.name = "***Candlestick for " + symbol + "***";
        request.channels = Channels.candlestickParams(symbol.toLowerCase(), interval);
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
        WebsocketRequest<OrderBookEvent> request = new BinanceWebsocketRequest<>(subscriptionListener, errorHandler);
        request.id= DigestUtils.md5Hex(Channels.bookDepthChannel(symbol.toLowerCase(), limit));
        request.name = "***Partial Book Depth for " + symbol + "***";
        request.channels = Channels.bookDepthParams(symbol.toLowerCase(), limit);
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
    public WebsocketRequest<SymbolTickerEvent> subscribeSymbolTickerEvent(String symbol, FutureSubscriptionListener<SymbolTickerEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<SymbolTickerEvent> request = new BinanceWebsocketRequest<>(subscriptionListener, errorHandler);
        request.id= DigestUtils.md5Hex(Channels.tickerChannel(symbol.toLowerCase()));
        request.name = "***Individual Symbol Ticker for " + symbol + "***";
        request.channels = Channels.tickerParams(symbol.toLowerCase());
        request.connectionHandler = (connection) -> connection.send(Channels.tickerChannel(symbol.toLowerCase()));

        request.jsonParser = (jsonWrapper) -> {
            SymbolTickerEvent result = new SymbolTickerEvent();
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
    public WebsocketRequest<List<SymbolTickerEvent>> subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolTickerEvent>> subscriptionListener,
                                                                             FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<List<SymbolTickerEvent>> request = new BinanceWebsocketRequest<>(subscriptionListener, errorHandler);
        request.id= DigestUtils.md5Hex(Channels.tickerChannel());
        request.name = "***All Market Tickers";
        request.channels = Collections.singletonList("!ticker@arr");
        request.connectionHandler = (connection) -> connection.send(Channels.tickerChannel());

        request.jsonParser = (jsonWrapper) -> {
            List<SymbolTickerEvent> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach(item -> {
                SymbolTickerEvent element = new SymbolTickerEvent();
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
        WebsocketRequest<SymbolBookTickerEvent> request = new BinanceWebsocketRequest<>(subscriptionListener, errorHandler);
        request.id= DigestUtils.md5Hex(Channels.bookTickerChannel(symbol.toLowerCase()));
        request.name = "***Individual Symbol Book Ticker for " + symbol + "***";
        request.channels = Channels.bookTickerParams(symbol.toLowerCase());
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
        WebsocketRequest<SymbolBookTickerEvent> request = new BinanceWebsocketRequest<>(subscriptionListener, errorHandler);
        request.id= DigestUtils.md5Hex(Channels.bookTickerChannel());
        request.name = "***All Market Book Tickers***";
        request.channels = Collections.singletonList("!bookTicker");
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
    public WebsocketRequest<List<BalanceUpdateEvent>> subscribeAccountEvent(String currency, FutureSubscriptionListener<List<BalanceUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(this.privateToken, "listenKey")
                .shouldNotNull(callback, "listener");
        WebsocketRequest<List<BalanceUpdateEvent>> request = new BinanceWebsocketRequest<>(callback, errorHandler);
        request.id= DigestUtils.md5Hex(Channels.userDataChannel(this.privateToken));
        request.name = "***User Account***";
        request.channels = Collections.singletonList(this.privateToken);
        request.connectionHandler = (connection) -> connection.send(Channels.userDataChannel(this.privateToken));

        request.jsonParser = (jsonWrapper) -> {
            JsonWrapper data = jsonWrapper.getJsonObject("data");

            if (data.getString("e").equals("ACCOUNT_UPDATE")) {
                List<BalanceUpdateEvent> balanceList = new LinkedList<>();
                String eventType = data.getJsonObject("a").getString("m");
                JsonWrapperArray datalist = data.getJsonObject("a").getJsonArray("B");
                datalist.forEach(item -> {
                    String itemCurrency = item.getString("a");
                    if (StringUtils.isNotEmpty(currency) && !itemCurrency.equalsIgnoreCase(currency)) {
                        return;
                    }
                    BalanceUpdateEvent balance = new BalanceUpdateEvent();
                    balance.setEventType(eventType);
                    balance.setAsset(item.getString("a"));
                    balance.setWalletBalance(item.getBigDecimal("wb"));
                    balanceList.add(balance);
                });
                return balanceList;
            }
            return null;
        };
        return request;
    }

    @Override
    public WebsocketRequest<List<PositionUpdateEvent>> subscribePositionEvent(String symbol, FutureSubscriptionListener<List<PositionUpdateEvent>> callback,
                                                                              FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(this.privateToken, "listenKey")
                .shouldNotNull(callback, "listener");
        WebsocketRequest<List<PositionUpdateEvent>> request = new BinanceWebsocketRequest<>(callback, errorHandler);
        request.id= DigestUtils.md5Hex(Channels.userDataChannel(this.privateToken));
        request.name = "***User Position***";
        request.channels = Collections.singletonList(this.privateToken);
        request.connectionHandler = (connection) -> connection.send(Channels.userDataChannel(this.privateToken));
        request.jsonParser = (jsonWrapper) -> {
            JsonWrapper data = jsonWrapper.getJsonObject("data");

            if (data.getString("e").equals("ACCOUNT_UPDATE")) {
                List<PositionUpdateEvent> positionList = new LinkedList<>();
                String eventType = data.getJsonObject("a").getString("m");
                JsonWrapperArray datalist = data.getJsonObject("a").getJsonArray("P");
                datalist.forEach(item -> {
                    String itemSymbol = item.getString("s");
                    if (StringUtils.isNotEmpty(symbol) && !itemSymbol.equalsIgnoreCase(symbol)) {
                        return;
                    }
                    PositionUpdateEvent position = new PositionUpdateEvent();
                    position.setSymbol(itemSymbol);
                    position.setAmount(item.getBigDecimal("pa")); // 仓位
                    position.setEntryPrice(item.getBigDecimal("ep")); // 入仓价格
                    position.setSide(PositionSide.valueOf(item.getString("ps")));
                    position.setUnrealizedPnl(item.getBigDecimal("up")); // 持仓未实现盈亏
                    position.setEventType(eventType);
                    positionList.add(position);
                });
                return positionList;
            }
            return null;
        };
        return request;
    }

    @Override
    public WebsocketRequest<OrderUpdateEvent> subscribeOrderUpdateEvent(String symbol, FutureSubscriptionListener<OrderUpdateEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(this.privateToken, "listenKey")
                .shouldNotNull(callback, "listener");
        WebsocketRequest<OrderUpdateEvent> request = new BinanceWebsocketRequest<>(callback, errorHandler);
        request.id= DigestUtils.md5Hex(Channels.userDataChannel(this.privateToken));
        request.name = "***User Order***";
        request.channels = Collections.singletonList(this.privateToken);
        request.connectionHandler = (connection) -> connection.send(Channels.userDataChannel(this.privateToken));

        request.jsonParser = (jsonWrapper) -> {
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            if (data.getString("e").equals("ORDER_TRADE_UPDATE")) {
                OrderUpdateEvent orderUpdate = new OrderUpdateEvent();

                JsonWrapper jsondata = data.getJsonObject("o");
                String itemSymbol = jsondata.getStringOrDefault("s", "");
                orderUpdate.setSymbol(itemSymbol);

                if (StringUtils.isNotEmpty(symbol) && !itemSymbol.equalsIgnoreCase(symbol)) {
                    return null;
                }

                orderUpdate.setClientOrderId(jsondata.getString("c"));
                orderUpdate.setSide(OrderSide.valueOf(jsondata.getString("S")));
                orderUpdate.setType(OrderType.valueOf(jsondata.getString("o")));
                orderUpdate.setTimeInForce(jsondata.getString("f"));
                orderUpdate.setOrigQty(jsondata.getBigDecimal("q"));
                orderUpdate.setPrice(jsondata.getBigDecimal("p"));
                orderUpdate.setAvgPrice(jsondata.getBigDecimal("ap"));
                orderUpdate.setStopPrice(jsondata.getBigDecimal("sp"));
                orderUpdate.setExecutionType(jsondata.getString("x"));
                orderUpdate.setStatus(OrderStatus.valueOf(jsondata.getString("X")));
                orderUpdate.setOrderId(jsondata.getLong("i"));
                orderUpdate.setLastFilledQty(jsondata.getBigDecimal("l"));
                orderUpdate.setCumulativeFilledQty(jsondata.getBigDecimal("z"));
                orderUpdate.setLastFilledPrice(jsondata.getBigDecimal("L"));
                orderUpdate.setCommissionAsset(jsondata.getStringOrDefault("N", ""));
                orderUpdate.setCommissionAmount(jsondata.getBigDecimalOrDefault("n", BigDecimal.ZERO));
                orderUpdate.setOrderTradeTime(jsondata.getLong("T"));
                orderUpdate.setTradeId(jsondata.getLong("t"));
                orderUpdate.setBidsNotional(jsondata.getBigDecimal("b"));
                orderUpdate.setAsksNotional(jsondata.getBigDecimal("a"));
                orderUpdate.setIsMarkerSide(jsondata.getBoolean("m"));
                orderUpdate.setIsReduceOnly(jsondata.getBoolean("R"));
                orderUpdate.setWorkingType(jsondata.getString("wt"));
                orderUpdate.setActivationPrice(jsondata.getBigDecimalOrDefault("AP", BigDecimal.ZERO));
                orderUpdate.setCallbackRate(jsondata.getBigDecimalOrDefault("cr", BigDecimal.ZERO));
                return orderUpdate;
            }
            return null;
        };
        return request;
    }

}
