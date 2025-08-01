package org.herman.future.impl;

import org.herman.future.FutureSubscriptionOptions;
import org.herman.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WebSocketConnectionPool {
    private static final Logger log = LoggerFactory.getLogger(WebSocketConnectionPool.class);

    private final WebSocketWatchDog watchDog;
    private final FutureSubscriptionOptions options;
    private final List<WebSocketConnection> connections = new ArrayList<>();

    public WebSocketConnectionPool(FutureSubscriptionOptions options) {
        this.options = options;
        this.watchDog = new WebSocketWatchDog(options);
        if (options.getConnectionCount() <= 0) {
            throw new IllegalArgumentException("FutureSubscriptionOptions.connectionCount must gt 0");
        }

        for (int i = 0; i < options.getConnectionCount(); i++) {
            WebSocketConnection connection = new WebSocketConnection(options, watchDog);
            connection.connect();
            connections.add(connection);
        }
    }

    public synchronized WebSocketConnection get() {
        if (connections.size() < options.getConnectionCount()) {
            WebSocketConnection connection = new WebSocketConnection(options, watchDog);
            connection.connect();
            Utils.sleepSeconds(1);
            connections.add(connection);
        }

        int index = (int) (Math.random() * connections.size());
        return connections.get(index);
    }


    public synchronized void close(WebSocketConnection connection) {
        watchDog.onClosedNormally(connection);
        connection.close();
        connections.remove(connection);
    }


    public synchronized void closeAll() {
        for (WebSocketConnection connection : connections) {
            watchDog.onClosedNormally(connection);
            connection.close();
        }
        connections.clear();
    }


}
