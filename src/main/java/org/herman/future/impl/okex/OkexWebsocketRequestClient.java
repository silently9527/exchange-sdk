package org.herman.future.impl.okex;

import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.impl.WebsocketRequest;
import org.herman.future.impl.WebsocketRequestClient;
import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.event.*;
import org.herman.utils.InputChecker;
import org.herman.utils.JsonWrapper;
import org.herman.utils.JsonWrapperArray;

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
        return null;
    }

    @Override
    public WebsocketRequest<SymbolMiniTickerEvent> subscribeSymbolMiniTickerEvent(String symbol, FutureSubscriptionListener<SymbolMiniTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }

    @Override
    public WebsocketRequest<List<SymbolMiniTickerEvent>> subscribeAllMiniTickerEvent(FutureSubscriptionListener<List<SymbolMiniTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }

    @Override
    public WebsocketRequest<SymbolBookTickerEvent> subscribeSymbolBookTickerEvent(String symbol, FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }

    @Override
    public WebsocketRequest<SymbolBookTickerEvent> subscribeAllBookTickerEvent(FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }
}
