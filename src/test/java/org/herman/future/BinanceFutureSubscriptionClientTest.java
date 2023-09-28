package org.herman.future;

import org.herman.Constants;
import org.herman.exception.ApiException;
import org.herman.future.impl.WebSocketFutureSubscriptionClient;
import org.herman.future.impl.WebsocketRequestClient;
import org.herman.future.impl.binance.BinanceWebsocketRequestClient;
import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.event.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BinanceFutureSubscriptionClientTest {


    @Test
    public void subscribeAggregateTradeEvent() throws InterruptedException {

        FutureSubscriptionOptions options = new FutureSubscriptionOptions();
        options.setUri(Constants.Future.BINANCE_WS_API_BASE_URL);
        WebsocketRequestClient requestImpl = new BinanceWebsocketRequestClient();
        FutureSubscriptionClient subscriptionClient = new WebSocketFutureSubscriptionClient(options, requestImpl);

        String symbol = "ETHUSDT".toLowerCase();

//        subscriptionClient.subscribeAggregateTradeEvent(symbol, new FutureSubscriptionListener<AggregateTradeEvent>() {
//            @Override
//            public void onReceive(AggregateTradeEvent data) {
//                System.out.println(data);
//            }
//        }, new FutureSubscriptionErrorHandler() {
//            @Override
//            public void onError(ApiException exception) {
//                exception.printStackTrace();
//            }
//        });
//        subscriptionClient.subscribeMarkPriceEvent(symbol, new FutureSubscriptionListener<MarkPriceEvent>() {
//            @Override
//            public void onReceive(MarkPriceEvent data) {
//                System.out.println(data);
//            }
//        }, new FutureSubscriptionErrorHandler() {
//            @Override
//            public void onError(ApiException exception) {
//                exception.printStackTrace();
//            }
//        });


//        subscriptionClient.subscribeCandlestickEvent(symbol, CandlestickInterval.ONE_MINUTE, new FutureSubscriptionListener<CandlestickEvent>() {
//            @Override
//            public void onReceive(CandlestickEvent data) {
//                System.out.println(data);
//            }
//        }, new FutureSubscriptionErrorHandler() {
//            @Override
//            public void onError(ApiException exception) {
//            }
//        });
//

//        subscriptionClient.subscribeBookDepthEvent(symbol, 5, new FutureSubscriptionListener<OrderBookEvent>() {
//            @Override
//            public void onReceive(OrderBookEvent data) {
//                System.out.println(data);
//            }
//        }, new FutureSubscriptionErrorHandler() {
//            @Override
//            public void onError(ApiException exception) {
//
//            }
//        });

//        subscriptionClient.subscribeSymbolMiniTickerEvent(symbol, new FutureSubscriptionListener<SymbolMiniTickerEvent>() {
//            @Override
//            public void onReceive(SymbolMiniTickerEvent data) {
//                System.out.println(data);
//            }
//        }, new FutureSubscriptionErrorHandler() {
//            @Override
//            public void onError(ApiException exception) {
//            }
//        });

//        subscriptionClient.subscribeAllMiniTickerEvent(new FutureSubscriptionListener<List<SymbolMiniTickerEvent>>() {
//            @Override
//            public void onReceive(List<SymbolMiniTickerEvent> data) {
//                System.out.println(data);
//            }
//        }, new FutureSubscriptionErrorHandler() {
//            @Override
//            public void onError(ApiException exception) {
//            }
//        });

//        subscriptionClient.subscribeSymbolBookTickerEvent(symbol, new FutureSubscriptionListener<SymbolBookTickerEvent>() {
//            @Override
//            public void onReceive(SymbolBookTickerEvent data) {
//                System.out.println(data);
//            }
//        }, new FutureSubscriptionErrorHandler() {
//            @Override
//            public void onError(ApiException exception) {
//            }
//        });

        subscriptionClient.subscribeAllBookTickerEvent(new FutureSubscriptionListener<SymbolBookTickerEvent>() {
            @Override
            public void onReceive(SymbolBookTickerEvent data) {
                System.out.println(data);
            }
        }, new FutureSubscriptionErrorHandler() {
            @Override
            public void onError(ApiException exception) {

            }
        });
        Thread.sleep(60000 * 10);
    }


}