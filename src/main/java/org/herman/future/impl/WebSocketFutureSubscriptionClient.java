package org.herman.future.impl;

import org.herman.future.FutureSubscriptionClient;
import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.FutureSubscriptionOptions;
import org.herman.future.model.event.AggregateTradeEvent;

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
                                             FutureSubscriptionListener<AggregateTradeEvent> subscriptionListener,
                                             FutureSubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeAggregateTradeEvent(symbol, subscriptionListener, errorHandler));
    }

}
