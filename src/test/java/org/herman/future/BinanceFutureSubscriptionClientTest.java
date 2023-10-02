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

        FutureSubscriptionOptions options = new FutureSubscriptionOptions(Constants.Future.BINANCE_WS_API_BASE_URL, "", "");
        WebsocketRequestClient requestImpl = new BinanceWebsocketRequestClient(options);
        FutureSubscriptionClient subscriptionClient = new WebSocketFutureSubscriptionClient(options, requestImpl);

        String symbol = "ETHUSDT".toLowerCase();

//        subscriptionClient.subscribeAggregateTradeEvent(symbol, System.out::println, null);
//        subscriptionClient.subscribeMarkPriceEvent(symbol, System.out::println,null);
//        subscriptionClient.subscribeCandlestickEvent(symbol, CandlestickInterval.ONE_MINUTE,System.out::println, null);
//        subscriptionClient.subscribeBookDepthEvent(symbol, 5, System.out::println,null);
//        subscriptionClient.subscribeSymbolMiniTickerEvent(symbol,System.out::println, null);
//        subscriptionClient.subscribeAllMiniTickerEvent(System.out::println, null);
//        subscriptionClient.subscribeSymbolBookTickerEvent(symbol, System.out::println,null);
        subscriptionClient.subscribeAllBookTickerEvent(System.out::println, null);
        Thread.sleep(60000 * 10);
    }


}