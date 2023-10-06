package org.herman.future;

import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.event.*;
import org.herman.future.model.user.BalanceUpdateEvent;
import org.herman.future.model.user.OrderUpdateEvent;
import org.herman.future.model.user.PositionUpdateEvent;

import java.util.List;

/***
 * The subscription client interface, it is used for subscribing any market data
 * update and account change, it is asynchronous, so you must implement the
 * FutureSubscriptionListener interface. The server will push any update to the
 * client. if client get the update, the onReceive method will be called.
 */
public interface FutureSubscriptionClient {

    void unsubscribeAll();

    void subscribeAggregateTradeEvent(String symbol, FutureSubscriptionListener<AggregateTradeEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    void subscribeMarkPriceEvent(String symbol, FutureSubscriptionListener<MarkPriceEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    void subscribeCandlestickEvent(String symbol, CandlestickInterval interval, FutureSubscriptionListener<CandlestickEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    //最新成交价
    void subscribeSymbolTickerEvent(String symbol, FutureSubscriptionListener<SymbolTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    void subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler);

//    void subscribeSymbolTickerEvent(String symbol,
//            FutureSubscriptionListener<SymbolTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler);
//
//    void subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler);

    void subscribeSymbolBookTickerEvent(String symbol, FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    void subscribeAllBookTickerEvent(FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    //    void subscribeSymbolLiquidationOrderEvent(String symbol,
//            FutureSubscriptionListener<LiquidationOrderEvent> callback, FutureSubscriptionErrorHandler errorHandler);
//
//    void subscribeAllLiquidationOrderEvent(FutureSubscriptionListener<LiquidationOrderEvent> callback, FutureSubscriptionErrorHandler errorHandler);
//
    //limit: 5 10 20
    void subscribeBookDepthEvent(String symbol, Integer limit,
                                 FutureSubscriptionListener<OrderBookEvent> callback, FutureSubscriptionErrorHandler errorHandler);

//    void subscribeDiffDepthEvent(String symbol, FutureSubscriptionListener<OrderBookEvent> callback, FutureSubscriptionErrorHandler errorHandler);

    void subscribePositionEvent(String symbol, FutureSubscriptionListener<List<PositionUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler);

    void subscribeAccountEvent(String currency, FutureSubscriptionListener<List<BalanceUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler);

    void subscribeOrderUpdateEvent(String symbol, FutureSubscriptionListener<OrderUpdateEvent> callback, FutureSubscriptionErrorHandler errorHandler);


}
