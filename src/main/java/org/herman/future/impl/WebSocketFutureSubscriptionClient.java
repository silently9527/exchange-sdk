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

import java.util.List;

public class WebSocketFutureSubscriptionClient implements FutureSubscriptionClient {
    private final WebSocketConnectionPool webSocketConnectionPool;
    private final WebsocketRequestClient requestImpl;

    public WebSocketFutureSubscriptionClient(FutureSubscriptionOptions options, WebsocketRequestClient requestImpl) {
        this.requestImpl = requestImpl;
        webSocketConnectionPool = new WebSocketConnectionPool(options);
    }

    @Override
    public void unsubscribeAll() {
        webSocketConnectionPool.closeAll();
    }

    @Override
    public void unsubscribe(WebSocketConnection connection) {
        webSocketConnectionPool.close(connection);
    }

    @Override
    public WebSocketConnection subscribeAggregateTradeEvent(String symbol,
                                                            FutureSubscriptionListener<AggregateTradeEvent> callback,
                                                            FutureSubscriptionErrorHandler errorHandler) {
        final WebsocketRequest<AggregateTradeEvent> request = requestImpl.subscribeAggregateTradeEvent(symbol, callback, errorHandler);
        final WebSocketConnection connection = webSocketConnectionPool.get();
        connection.addRequest(request);
        request.connectionHandler.handle(connection);
        return connection;
    }

    @Override
    public WebSocketConnection subscribeMarkPriceEvent(String symbol, FutureSubscriptionListener<MarkPriceEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        final WebsocketRequest<MarkPriceEvent> request = requestImpl.subscribeMarkPriceEvent(symbol, callback, errorHandler);
        final WebSocketConnection connection = webSocketConnectionPool.get();
        connection.addRequest(request);
        request.connectionHandler.handle(connection);
        return connection;
    }

    @Override
    public WebSocketConnection subscribeCandlestickEvent(String symbol, CandlestickInterval interval, FutureSubscriptionListener<CandlestickEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        final WebsocketRequest<CandlestickEvent> request = requestImpl.subscribeCandlestickEvent(symbol, interval, callback, errorHandler);
        final WebSocketConnection connection = webSocketConnectionPool.get();
        connection.addRequest(request);
        request.connectionHandler.handle(connection);
        return connection;
    }

    @Override
    public WebSocketConnection subscribeSymbolTickerEvent(String symbol, FutureSubscriptionListener<SymbolTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        final WebsocketRequest<SymbolTickerEvent> request = requestImpl.subscribeSymbolTickerEvent(symbol, callback, errorHandler);
        final WebSocketConnection connection = webSocketConnectionPool.get();
        connection.addRequest(request);
        request.connectionHandler.handle(connection);
        return connection;
    }

    @Override
    public WebSocketConnection subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        final WebsocketRequest<List<SymbolTickerEvent>> request = requestImpl.subscribeAllTickerEvent(callback, errorHandler);
        final WebSocketConnection connection = webSocketConnectionPool.get();
        connection.addRequest(request);
        request.connectionHandler.handle(connection);
        return connection;
    }

    @Override
    public WebSocketConnection subscribeSymbolBookTickerEvent(String symbol, FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        final WebsocketRequest<SymbolBookTickerEvent> request = requestImpl.subscribeSymbolBookTickerEvent(symbol, callback, errorHandler);
        final WebSocketConnection connection = webSocketConnectionPool.get();
        connection.addRequest(request);
        request.connectionHandler.handle(connection);
        return connection;
    }

    @Override
    public WebSocketConnection subscribeAllBookTickerEvent(FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        final WebsocketRequest<SymbolBookTickerEvent> request = requestImpl.subscribeAllBookTickerEvent(callback, errorHandler);
        final WebSocketConnection connection = webSocketConnectionPool.get();
        connection.addRequest(request);
        request.connectionHandler.handle(connection);
        return connection;
    }

    @Override
    public WebSocketConnection subscribeBookDepthEvent(String symbol, Integer limit, FutureSubscriptionListener<OrderBookEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        final WebsocketRequest<OrderBookEvent> request = requestImpl.subscribeBookDepthEvent(symbol, limit, callback, errorHandler);
        final WebSocketConnection connection = webSocketConnectionPool.get();
        connection.addRequest(request);
        request.connectionHandler.handle(connection);
        return connection;
    }

    @Override
    public WebSocketConnection subscribePositionEvent(String symbol, FutureSubscriptionListener<List<PositionUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        final WebsocketRequest<List<PositionUpdateEvent>> request = requestImpl.subscribePositionEvent(symbol, callback, errorHandler);
        final WebSocketConnection connection = webSocketConnectionPool.get();
        connection.addRequest(request);
        request.connectionHandler.handle(connection);
        return connection;
    }

    @Override
    public WebSocketConnection subscribeAccountEvent(String currency, FutureSubscriptionListener<List<BalanceUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        final WebsocketRequest<List<BalanceUpdateEvent>> request = requestImpl.subscribeAccountEvent(currency, callback, errorHandler);
        final WebSocketConnection connection = webSocketConnectionPool.get();
        connection.addRequest(request);
        request.connectionHandler.handle(connection);
        return connection;
    }

    @Override
    public WebSocketConnection subscribeOrderUpdateEvent(String symbol, FutureSubscriptionListener<OrderUpdateEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        final WebsocketRequest<OrderUpdateEvent> request = requestImpl.subscribeOrderUpdateEvent(symbol, callback, errorHandler);
        final WebSocketConnection connection = webSocketConnectionPool.get();
        connection.addRequest(request);
        request.connectionHandler.handle(connection);
        return connection;
    }

}
