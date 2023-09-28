package org.herman.future.impl;

import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.event.*;

import java.util.List;

public interface WebsocketRequestClient {
    WebsocketRequest<AggregateTradeEvent> subscribeAggregateTradeEvent(String symbol, FutureSubscriptionListener<AggregateTradeEvent> subscriptionListener,
                                                                       FutureSubscriptionErrorHandler errorHandler);

    WebsocketRequest<MarkPriceEvent> subscribeMarkPriceEvent(String symbol, FutureSubscriptionListener<MarkPriceEvent> subscriptionListener,
                                                             FutureSubscriptionErrorHandler errorHandler);

    WebsocketRequest<CandlestickEvent> subscribeCandlestickEvent(String symbol, CandlestickInterval interval, FutureSubscriptionListener<CandlestickEvent> subscriptionListener,
                                                                 FutureSubscriptionErrorHandler errorHandler);

    WebsocketRequest<OrderBookEvent> subscribeBookDepthEvent(String symbol, Integer limit, FutureSubscriptionListener<OrderBookEvent> callback,
                                                             FutureSubscriptionErrorHandler errorHandler);

    WebsocketRequest<SymbolMiniTickerEvent> subscribeSymbolMiniTickerEvent(String symbol, FutureSubscriptionListener<SymbolMiniTickerEvent> callback,
                                                                           FutureSubscriptionErrorHandler errorHandler);

    WebsocketRequest<List<SymbolMiniTickerEvent>> subscribeAllMiniTickerEvent(FutureSubscriptionListener<List<SymbolMiniTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler);

    WebsocketRequest<SymbolBookTickerEvent> subscribeSymbolBookTickerEvent(String symbol, FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    WebsocketRequest<SymbolBookTickerEvent> subscribeAllBookTickerEvent(FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler);
}
