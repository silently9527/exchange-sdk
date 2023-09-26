package org.herman.future.impl;

import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.model.event.AggregateTradeEvent;

public interface WebsocketRequestClient {
    WebsocketRequest<AggregateTradeEvent> subscribeAggregateTradeEvent(String symbol,
                                                                       FutureSubscriptionListener<AggregateTradeEvent> subscriptionListener,
                                                                       FutureSubscriptionErrorHandler errorHandler);
}
