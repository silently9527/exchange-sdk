package org.herman.future.impl.binance;

import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.impl.WebsocketRequest;

public class BinanceWebsocketRequest<T> extends WebsocketRequest<T> {

    public BinanceWebsocketRequest(FutureSubscriptionListener<T> listener, FutureSubscriptionErrorHandler errorHandler) {
        super(listener, errorHandler);
        this.healthHandler = (connection) -> connection.updateLastReceivedTime(System.currentTimeMillis());
        this.responseValidator = (json) -> true;
    }

}
