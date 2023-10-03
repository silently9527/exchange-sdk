package org.herman.future.impl;

import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.FutureSubscriptionOptions;
import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.event.*;
import org.herman.future.model.user.BalanceUpdateEvent;
import org.herman.future.model.user.OrderUpdateEvent;
import org.herman.future.model.user.PositionUpdateEvent;

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

    //最新成交价
    WebsocketRequest<SymbolMiniTickerEvent> subscribeSymbolTickerEvent(String symbol, FutureSubscriptionListener<SymbolMiniTickerEvent> callback,
                                                                       FutureSubscriptionErrorHandler errorHandler);

    WebsocketRequest<List<SymbolMiniTickerEvent>> subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolMiniTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler);

    //最优挂单
    WebsocketRequest<SymbolBookTickerEvent> subscribeSymbolBookTickerEvent(String symbol, FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    WebsocketRequest<SymbolBookTickerEvent> subscribeAllBookTickerEvent(FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    WebsocketRequest<String> authentication(FutureSubscriptionListener<String> callback, FutureSubscriptionErrorHandler errorHandler);

    //获取listenerKey

    String listenerKey(FutureSubscriptionOptions options);

    WebsocketRequest<List<BalanceUpdateEvent>> subscribeAccountEvent(String listenerKey, String currency, FutureSubscriptionListener<List<BalanceUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler);

    WebsocketRequest<List<PositionUpdateEvent>> subscribePositionEvent(String listenerKey, String symbol, FutureSubscriptionListener<List<PositionUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler);

    WebsocketRequest<OrderUpdateEvent> subscribeOrderUpdateEvent(String listenerKey, String symbol, FutureSubscriptionListener<OrderUpdateEvent> callback, FutureSubscriptionErrorHandler errorHandler);
}
