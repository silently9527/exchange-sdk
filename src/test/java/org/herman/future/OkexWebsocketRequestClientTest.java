package org.herman.future;

import org.herman.Constants;
import org.herman.future.impl.WebSocketFutureSubscriptionClient;
import org.herman.future.impl.WebsocketRequestClient;
import org.herman.future.impl.binance.BinanceWebsocketRequestClient;
import org.herman.future.impl.okex.OkexWebsocketRequestClient;
import org.herman.future.model.enums.CandlestickInterval;
import org.junit.Test;

import static org.junit.Assert.*;

public class OkexWebsocketRequestClientTest {

    @Test
    public void test() throws InterruptedException {
        String symbol = "ETH-USDT-SWAP";


        FutureSubscriptionOptions options = new FutureSubscriptionOptions();
        options.setUri(Constants.Future.OKEX_BUSINESS_WS_API_BASE_URL);
        WebsocketRequestClient requestImpl = new OkexWebsocketRequestClient();
        FutureSubscriptionClient subscriptionClient = new WebSocketFutureSubscriptionClient(options, requestImpl);

        subscriptionClient.subscribeCandlestickEvent(symbol, CandlestickInterval.ONE_MINUTE, System.out::println, null);


        options = new FutureSubscriptionOptions();
        options.setUri(Constants.Future.OKEX_PUBLIC_WS_API_BASE_URL);
        requestImpl = new OkexWebsocketRequestClient();
        subscriptionClient = new WebSocketFutureSubscriptionClient(options, requestImpl);

//        subscriptionClient.subscribeMarkPriceEvent(symbol, System.out::println, null);


        Thread.sleep(60000 * 10);
    }


}