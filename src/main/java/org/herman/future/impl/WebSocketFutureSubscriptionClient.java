package org.herman.future.impl;

import org.herman.future.FutureSubscriptionClient;
import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.FutureSubscriptionOptions;
import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.event.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class WebSocketFutureSubscriptionClient implements FutureSubscriptionClient {
    private final FutureSubscriptionOptions options;
    private WebSocketWatchDog watchDog;
    private final List<WebSocketConnection> connections = new LinkedList<>();

    private final WebsocketRequestClient requestImpl;

    public WebSocketFutureSubscriptionClient(FutureSubscriptionOptions options, WebsocketRequestClient requestImpl) {
        this.watchDog = null;
        this.options = Objects.requireNonNull(options);

        this.requestImpl = requestImpl;
    }

    private <T> void createConnection(WebsocketRequest<T> request, boolean autoClose) {
        if (watchDog == null) {
            watchDog = new WebSocketWatchDog(options);
        }
        WebSocketConnection connection = new WebSocketConnection(request, watchDog, autoClose);
        if (!autoClose) {
            connections.add(connection);
        }
        connection.connect();
    }

    private <T> void createConnection(WebsocketRequest<T> request) {
        createConnection(request, false);
    }

    @Override
    public void unsubscribeAll() {
        for (WebSocketConnection connection : connections) {
            watchDog.onClosedNormally(connection);
            connection.close();
        }
        connections.clear();
    }


    @Override
    public void subscribeAggregateTradeEvent(String symbol,
                                             FutureSubscriptionListener<AggregateTradeEvent> callback,
                                             FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeAggregateTradeEvent(symbol, callback, errorHandler));
    }

    @Override
    public void subscribeMarkPriceEvent(String symbol, FutureSubscriptionListener<MarkPriceEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeMarkPriceEvent(symbol, callback, errorHandler));
    }

    @Override
    public void subscribeCandlestickEvent(String symbol, CandlestickInterval interval, FutureSubscriptionListener<CandlestickEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeCandlestickEvent(symbol, interval, callback, errorHandler));
    }

    @Override
    public void subscribeSymbolMiniTickerEvent(String symbol, FutureSubscriptionListener<SymbolMiniTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeSymbolMiniTickerEvent(symbol, callback, errorHandler));
    }

    @Override
    public void subscribeAllMiniTickerEvent(FutureSubscriptionListener<List<SymbolMiniTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeAllMiniTickerEvent(callback, errorHandler));
    }

    @Override
    public void subscribeSymbolBookTickerEvent(String symbol, FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeSymbolBookTickerEvent(symbol, callback, errorHandler));
    }

    @Override
    public void subscribeAllBookTickerEvent(FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeAllBookTickerEvent(callback, errorHandler));
    }

    @Override
    public void subscribeBookDepthEvent(String symbol, Integer limit, FutureSubscriptionListener<OrderBookEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeBookDepthEvent(symbol, limit, callback, errorHandler));
    }

}
