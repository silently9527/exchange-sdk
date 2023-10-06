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
    private String appKey = "rwuskEE2WtxPuG9cY9v0bHC0EVWsiz92chTWTY1m8lywFLF1AYbZFi2RWI33xV4C";
    private String secret = "cZbeVqjqN6wDkDpM2GdN899YDvEstzHT19t5PnwDbdvMUaUD9WvivPdh7lVdqCZg";

    @Test
    public void subscribeAggregateTradeEvent() throws InterruptedException {

        FutureSubscriptionClient subscriptionClient = FutureApiInternalFactory
                .getInstance()
                .createBinanceFutureSubscriptionClient(Constants.Future.BINANCE_WS_API_BASE_URL, appKey, secret);

        String symbol = "ETHUSDT".toLowerCase();

//        subscriptionClient.subscribeAggregateTradeEvent(symbol, System.out::println, null);
//        subscriptionClient.subscribeMarkPriceEvent(symbol, System.out::println,null);
//        subscriptionClient.subscribeCandlestickEvent(symbol, CandlestickInterval.ONE_MINUTE,System.out::println, null);
//        subscriptionClient.subscribeBookDepthEvent(symbol, 5, System.out::println,null);
//        subscriptionClient.subscribeSymbolMiniTickerEvent(symbol,System.out::println, null);
//        subscriptionClient.subscribeAllMiniTickerEvent(System.out::println, null);
//        subscriptionClient.subscribeSymbolBookTickerEvent(symbol, System.out::println,null);
//        subscriptionClient.subscribeAllBookTickerEvent(System.out::println, null);


//        subscriptionClient.subscribePositionEvent(symbol, System.out::println, null);
//        subscriptionClient.subscribePositionEvent("", System.out::println, null);
//        subscriptionClient.subscribeAccountEvent("", System.out::println, null);
        subscriptionClient.subscribeOrderUpdateEvent("ETHUSDT", System.out::println, null);


        Thread.sleep(60000 * 10);
    }


}