package org.herman.future.impl.kucoin;

import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.impl.WebsocketRequest;

public class KucoinWebsocketRequest<T> extends WebsocketRequest<T> {

    public KucoinWebsocketRequest(FutureSubscriptionListener<T> listener, FutureSubscriptionErrorHandler errorHandler) {
        super(listener, errorHandler);
        this.healthHandler = (connection) -> connection.send(Channels.ping());
        this.responseValidator = (json) -> true;
    }

}
