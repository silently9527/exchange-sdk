package org.herman.future;

import org.herman.future.impl.WebSocketConnection;
import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.event.*;
import org.herman.future.model.user.BalanceUpdateEvent;
import org.herman.future.model.user.OrderUpdateEvent;
import org.herman.future.model.user.PositionUpdateEvent;
import org.herman.utils.JsonWrapper;

import java.util.List;

/***
 * The subscription client interface, it is used for subscribing any market data
 * update and account change, it is asynchronous, so you must implement the
 * FutureSubscriptionListener interface. The server will push any update to the
 * client. if client get the update, the onReceive method will be called.
 */
public interface FutureSubscriptionClient {

    void unsubscribeAll();

    void unsubscribe(WebSocketConnection connection, List<String> channels, FutureSubscriptionListener<JsonWrapper> callback, FutureSubscriptionErrorHandler errorHandler);

    WebSocketConnection subscribeAggregateTradeEvent(String symbol, FutureSubscriptionListener<AggregateTradeEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    WebSocketConnection subscribeMarkPriceEvent(String symbol, FutureSubscriptionListener<MarkPriceEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    WebSocketConnection subscribeCandlestickEvent(String symbol, CandlestickInterval interval, FutureSubscriptionListener<CandlestickEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    //最新成交价
    WebSocketConnection subscribeSymbolTickerEvent(String symbol, FutureSubscriptionListener<SymbolTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    WebSocketConnection subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler);

//    void subscribeSymbolTickerEvent(String symbol,
//            FutureSubscriptionListener<SymbolTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler);
//
//    void subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler);

    WebSocketConnection subscribeSymbolBookTickerEvent(String symbol, FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    WebSocketConnection subscribeAllBookTickerEvent(FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    //    void subscribeSymbolLiquidationOrderEvent(String symbol,
//            FutureSubscriptionListener<LiquidationOrderEvent> callback, FutureSubscriptionErrorHandler errorHandler);
//
//    void subscribeAllLiquidationOrderEvent(FutureSubscriptionListener<LiquidationOrderEvent> callback, FutureSubscriptionErrorHandler errorHandler);
//
    //limit: 5 10 20
    WebSocketConnection subscribeBookDepthEvent(String symbol, Integer limit,
                                                FutureSubscriptionListener<OrderBookEvent> callback, FutureSubscriptionErrorHandler errorHandler);

//    void subscribeDiffDepthEvent(String symbol, FutureSubscriptionListener<OrderBookEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    WebSocketConnection subscribePositionEvent(String symbol, FutureSubscriptionListener<List<PositionUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler);

    WebSocketConnection subscribeAccountEvent(String currency, FutureSubscriptionListener<List<BalanceUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler);

    WebSocketConnection subscribeOrderUpdateEvent(String symbol, FutureSubscriptionListener<OrderUpdateEvent> callback, FutureSubscriptionErrorHandler errorHandler);

}
