package org.herman.future.impl;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.apache.commons.lang3.StringUtils;
import org.herman.exception.ApiException;
import org.herman.future.FutureSubscriptionOptions;
import org.herman.future.RestApiInvoker;
import org.herman.utils.JsonWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class WebSocketConnection extends WebSocketListener {
    private static final Logger log = LoggerFactory.getLogger(WebSocketConnection.class);
    private static int connectionCounter = 0;

    public enum ConnectionState {
        IDLE, DELAY_CONNECT, CONNECTED, CLOSED_ON_ERROR
    }

    private WebSocket webSocket = null;
    private volatile long lastReceivedTime = 0;

    private volatile ConnectionState state = ConnectionState.IDLE;
    private int delayInSecond = 0;

    private final List<WebsocketRequest> requests = new CopyOnWriteArrayList<>();
    private final Request okhttpRequest;
    private final int connectionId;

    public WebSocketConnection(FutureSubscriptionOptions options, WebSocketWatchDog watchDog) {
        this.connectionId = WebSocketConnection.connectionCounter++;

        this.okhttpRequest = new Request.Builder().url(options.getUri()).build();
        log.info("[Sub] Connection [id: " + this.connectionId + "] created");
    }

    public synchronized void addRequest(WebsocketRequest request) {
        requests.add(request);
    }

    int getConnectionId() {
        return this.connectionId;
    }

    void connect() {
        if (state == ConnectionState.CONNECTED) {
            log.info("[Sub][" + this.connectionId + "] Already connected");
            return;
        }
        log.info("[Sub][" + this.connectionId + "] Connecting...");
        webSocket = RestApiInvoker.createWebSocket(okhttpRequest, this);
    }

    void reConnect(int delayInSecond) {
        log.warn("[Sub][" + this.connectionId + "] Reconnecting after " + delayInSecond + " seconds later");
        if (webSocket != null) {
//            webSocket.cancel();
            webSocket.close(1000, "closing connection");
            webSocket = null;
        }
        this.delayInSecond = delayInSecond;
        state = ConnectionState.DELAY_CONNECT;
    }

    void reConnect() {
        if (delayInSecond != 0) {
            delayInSecond--;
        } else {
            connect();
        }
    }

    long getLastReceivedTime() {
        return this.lastReceivedTime;
    }

    public void updateLastReceivedTime(long time) {
        this.lastReceivedTime = time;
    }

    public void send(String str) {
        boolean result = false;
        log.debug("[Send]{}", str);
        if (webSocket != null) {
            result = webSocket.send(str);
        }
        if (!result) {
            log.error("[Sub][" + this.connectionId + "] Failed to send message");
            closeOnError();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        lastReceivedTime = System.currentTimeMillis();

        log.debug("[On Message]:{}", text);
        try {
            JsonWrapper jsonWrapper = JsonWrapper.parseFromString(text);
            if ((jsonWrapper.containKey("event") && "subscribe".equals(jsonWrapper.getString("event")))
                    || jsonWrapper.containKey("result")
                    || (jsonWrapper.containKey("id") && !jsonWrapper.containKey("subject"))
                    || (jsonWrapper.containKey("type") && jsonWrapper.getString("type").equals("pong"))
            ) {
            } else {
                onReceive(jsonWrapper);
            }
        } catch (Exception e) {
            log.error("[On Message][{}]: catch exception:", connectionId, e);
            closeOnError();
        }
    }

    private void onError(String errorMessage, Throwable e) {
        requests.stream()
                .filter(request -> Objects.nonNull(request.errorHandler))
                .forEach(request -> {
                    ApiException exception = new ApiException(ApiException.SUBSCRIPTION_ERROR, errorMessage, e);
                    request.errorHandler.onError(exception);
                });
        log.error("[Sub][" + this.connectionId + "] " + errorMessage);
    }

    @SuppressWarnings("unchecked")
    private void onReceive(JsonWrapper jsonWrapper) {
        final String channel = parseChannel(jsonWrapper);
        if (StringUtils.isBlank(channel)) {
            log.error("解析channel失败, msg:{}", jsonWrapper.getJson());
            return;
        }
        final List<WebsocketRequest> matchedRequests = getMatchedRequests(channel);
        if (Objects.isNull(matchedRequests) || matchedRequests.isEmpty()) {
            return;
        }
        for (WebsocketRequest request : matchedRequests) {
            if (request.responseValidator.validate(jsonWrapper)) {
                Object obj = null;
                try {
                    obj = request.jsonParser.parseJson(jsonWrapper);
                } catch (Exception e) {
                    onError("Failed to parse server's response: " + e.getMessage(), e);
                }
                try {
                    if (Objects.nonNull(obj)) {
                        request.updateCallback.onReceive(obj);
                    }
                } catch (Exception e) {
                    onError("Process error: " + e.getMessage() + " You should capture the exception in your error handler", e);
                }
            }
        }
    }

    private List<WebsocketRequest> getMatchedRequests(String channel) {
        return requests.stream()
                .filter(request -> request.channels.contains(channel))
                .collect(Collectors.toList());
    }

    private String parseChannel(JsonWrapper jsonWrapper) {
        String channel = null;
        try {
            channel = jsonWrapper.getString("stream");
        } catch (Exception e) {
        }
        try {
            channel = jsonWrapper.getString("topic");
        } catch (Exception e) {
        }
        return channel;
    }

    public ConnectionState getState() {
        return state;
    }

    public void close() {
        log.info("[Sub][" + this.connectionId + "] Closing normally");
//        webSocket.cancel();
        webSocket.close(1000, "closing connection");
        webSocket = null;
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        if (state == ConnectionState.CONNECTED) {
            state = ConnectionState.IDLE;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        this.webSocket = webSocket;
        log.info("[Sub][" + this.connectionId + "] Connected to server");
        state = ConnectionState.CONNECTED;
        lastReceivedTime = System.currentTimeMillis();
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        onError("Unexpected error: " + t.getMessage(), t);
        closeOnError();
    }

    private void closeOnError() {
        if (webSocket != null) {
//            this.webSocket.cancel();
            webSocket.close(1000, "closing connection");
            state = ConnectionState.CLOSED_ON_ERROR;
            log.error("[Sub][" + this.connectionId + "] Connection is closing due to error");
        }
    }

    @SuppressWarnings("unchecked")
    public void ping() {
        requests.stream()
                .filter(request -> Objects.nonNull(request.healthHandler))
                .findFirst()
                .ifPresent(request -> {
                    request.healthHandler.handle(this);
                });
    }
}
