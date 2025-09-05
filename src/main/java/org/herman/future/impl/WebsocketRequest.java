package org.herman.future.impl;


import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.utils.Handler;

import java.util.List;

public class WebsocketRequest<T> {

    public WebsocketRequest(FutureSubscriptionListener<T> listener, FutureSubscriptionErrorHandler errorHandler) {
        this.updateCallback = listener;
        this.errorHandler = errorHandler;
    }

    String signatureVersion = "2";
    public String id;
    public String name;
    public List<String> channels;
    public Handler<WebSocketConnection> connectionHandler;
    //    public Handler<WebSocketConnection> authHandler = null;
    public Handler<WebSocketConnection> healthHandler;
    public WebsocketResponseValidator responseValidator;
    final FutureSubscriptionListener<T> updateCallback;
    public RestApiJsonParser<T> jsonParser;
    final FutureSubscriptionErrorHandler errorHandler;
}
