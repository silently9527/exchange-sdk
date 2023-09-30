package org.herman.future.impl.okex;

import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.impl.WebsocketRequest;
import org.herman.future.impl.WebsocketRequestClient;
import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.event.*;
import org.herman.future.model.market.OrderBookEntry;
import org.herman.utils.InputChecker;
import org.herman.utils.JsonWrapper;
import org.herman.utils.JsonWrapperArray;

import java.util.LinkedList;
import java.util.List;

public class OkexWebsocketRequestClient implements WebsocketRequestClient {
    @Override
    public WebsocketRequest<AggregateTradeEvent> subscribeAggregateTradeEvent(String symbol, FutureSubscriptionListener<AggregateTradeEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {

        return null;
    }

    @Override
    public WebsocketRequest<MarkPriceEvent> subscribeMarkPriceEvent(String symbol, FutureSubscriptionListener<MarkPriceEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<MarkPriceEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Mark Price for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.markPriceChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            JsonWrapper data = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);
            MarkPriceEvent result = new MarkPriceEvent();
            result.setEventType("MarkPrice");
            result.setEventTime(data.getLong("ts"));
            result.setSymbol(data.getString("instId"));
            result.setMarkPrice(data.getBigDecimal("markPx"));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<CandlestickEvent> subscribeCandlestickEvent(String symbol, CandlestickInterval interval, FutureSubscriptionListener<CandlestickEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<CandlestickEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Candlestick for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.candlestickChannel(symbol, interval));

        request.jsonParser = (jsonWrapper) -> {
            CandlestickEvent result = new CandlestickEvent();
            JsonWrapperArray data = jsonWrapper.getJsonArray("data").getArrayAt(0);
            JsonWrapper arg = jsonWrapper.getJsonObject("arg");

            result.setSymbol(arg.getString("instId"));
            result.setEventType("Candlestick");
            result.setEventTime(System.currentTimeMillis());

            result.setOpenTime(Long.parseLong(data.getStringAt(0)));
            result.setOpen(data.getBigDecimalAt(1));
            result.setHigh(data.getBigDecimalAt(2));
            result.setLow(data.getBigDecimalAt(3));
            result.setClose(data.getBigDecimalAt(4));
            result.setVolume(data.getBigDecimalAt(5));
            result.setQuoteAssetVolume(data.getBigDecimalAt(7));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<OrderBookEvent> subscribeBookDepthEvent(String symbol, Integer limit, FutureSubscriptionListener<OrderBookEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(callback, "listener");
        WebsocketRequest<OrderBookEvent> request = new WebsocketRequest<>(callback, errorHandler);
        request.name = "***Partial Book Depth for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.bookDepthChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            OrderBookEvent result = new OrderBookEvent();
            JsonWrapper data = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);

            result.setEventType("BookDepth");
            result.setEventTime(data.getLong("ts"));
            result.setTransactionTime(data.getLong("ts"));
            result.setSymbol(data.getString("instId"));
            result.setLastUpdateId(data.getLong("seqId"));

            List<OrderBookEntry> elementList = new LinkedList<>();
            JsonWrapperArray dataArray = data.getJsonArray("bids");
            dataArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice(item.getBigDecimalAt(0));
                element.setQty(item.getBigDecimalAt(1));
                elementList.add(element);
            });
            result.setBids(elementList);

            List<OrderBookEntry> askList = new LinkedList<>();
            JsonWrapperArray askArray = data.getJsonArray("asks");
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
    public WebsocketRequest<SymbolMiniTickerEvent> subscribeSymbolTickerEvent(String symbol, FutureSubscriptionListener<SymbolMiniTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(callback, "listener");
        WebsocketRequest<SymbolMiniTickerEvent> request = new WebsocketRequest<>(callback, errorHandler);
        request.name = "***Individual Symbol Ticker for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.tickerChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            SymbolMiniTickerEvent result = new SymbolMiniTickerEvent();
            JsonWrapper data = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);
            result.setEventType("SymbolTicker");
            result.setEventTime(data.getLong("ts"));
            result.setSymbol(data.getString("instId"));
            result.setPrice(data.getBigDecimal("last"));
            result.setVolume(data.getBigDecimal("lastSz"));
            result.setQuoteAssetVolume(data.getBigDecimal("volCcy24h"));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<List<SymbolMiniTickerEvent>> subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolMiniTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<SymbolBookTickerEvent> subscribeSymbolBookTickerEvent(String symbol, FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(callback, "listener");
        WebsocketRequest<SymbolBookTickerEvent> request = new WebsocketRequest<>(callback, errorHandler);
        request.name = "***Individual Symbol Ticker for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.tickerChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            SymbolBookTickerEvent result = new SymbolBookTickerEvent();
            JsonWrapper data = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);
            result.setOrderBookUpdateId(data.getLong("ts"));
            result.setSymbol(data.getString("instId"));
            result.setBestBidPrice(data.getBigDecimal("bidPx"));
            result.setBestBidQty(data.getBigDecimal("bidSz"));
            result.setBestAskPrice(data.getBigDecimal("askPx"));
            result.setBestAskQty(data.getBigDecimal("askSz"));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<SymbolBookTickerEvent> subscribeAllBookTickerEvent(FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }
}
