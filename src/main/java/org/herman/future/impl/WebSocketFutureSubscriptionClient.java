package org.herman.future.impl;

import org.herman.future.FutureSubscriptionClient;
import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.FutureSubscriptionOptions;
import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.event.*;
import org.herman.future.model.user.BalanceUpdateEvent;
import org.herman.future.model.user.OrderUpdateEvent;
import org.herman.future.model.user.PositionUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketFutureSubscriptionClient implements FutureSubscriptionClient {
    private final Logger logger = LoggerFactory.getLogger(WebSocketFutureSubscriptionClient.class);

    private final FutureSubscriptionOptions options;
    private WebSocketWatchDog watchDog;
    private final Map<Integer, WebSocketConnection> connections = new ConcurrentHashMap<>();

    private final WebsocketRequestClient requestImpl;

    public WebSocketFutureSubscriptionClient(FutureSubscriptionOptions options, WebsocketRequestClient requestImpl) {
        this.watchDog = null;
        this.options = Objects.requireNonNull(options);

        this.requestImpl = requestImpl;
    }

    private <T> WebSocketConnection createConnection(WebsocketRequest<T> request, boolean autoClose) {
        if (watchDog == null) {
            watchDog = new WebSocketWatchDog(options);
        }
        WebSocketConnection connection = new WebSocketConnection(request, options, watchDog, autoClose);
        if (!autoClose) {
            connections.put(connection.getConnectionId(), connection);
        }
        connection.connect();

        return connection;
    }

    private <T> WebSocketConnection createConnection(WebsocketRequest<T> request) {
        return createConnection(request, false);
    }

    @Override
    public void unsubscribeAll() {
        for (WebSocketConnection connection : connections.values()) {
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
    public void subscribeSymbolTickerEvent(String symbol, FutureSubscriptionListener<SymbolTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeSymbolTickerEvent(symbol, callback, errorHandler));
    }

    @Override
    public void subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeAllTickerEvent(callback, errorHandler));
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

    @Override
    public void subscribePositionEvent(String symbol, FutureSubscriptionListener<List<PositionUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribePositionEvent(symbol, callback, errorHandler));
    }

    @Override
    public void subscribeAccountEvent(String currency, FutureSubscriptionListener<List<BalanceUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeAccountEvent(currency, callback, errorHandler));
    }

    @Override
    public void subscribeOrderUpdateEvent(String symbol, FutureSubscriptionListener<OrderUpdateEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeOrderUpdateEvent(symbol, callback, errorHandler));
    }

}
