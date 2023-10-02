package org.herman.future.impl.kucoin;

import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.FutureSubscriptionOptions;
import org.herman.future.impl.WebsocketRequest;
import org.herman.future.impl.WebsocketRequestClient;
import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.event.*;

import java.util.List;

public class KucoinWebsocketRequestClient implements WebsocketRequestClient {
    @Override
    public WebsocketRequest<AggregateTradeEvent> subscribeAggregateTradeEvent(String symbol, FutureSubscriptionListener<AggregateTradeEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }

    @Override
    public WebsocketRequest<MarkPriceEvent> subscribeMarkPriceEvent(String symbol, FutureSubscriptionListener<MarkPriceEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }

    @Override
    public WebsocketRequest<CandlestickEvent> subscribeCandlestickEvent(String symbol, CandlestickInterval interval, FutureSubscriptionListener<CandlestickEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }

    @Override
    public WebsocketRequest<OrderBookEvent> subscribeBookDepthEvent(String symbol, Integer limit, FutureSubscriptionListener<OrderBookEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }

    @Override
    public WebsocketRequest<SymbolMiniTickerEvent> subscribeSymbolTickerEvent(String symbol, FutureSubscriptionListener<SymbolMiniTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }

    @Override
    public WebsocketRequest<List<SymbolMiniTickerEvent>> subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolMiniTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
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

    @Override
    public String listenerKey(FutureSubscriptionOptions options) {


        return null;
    }
}
