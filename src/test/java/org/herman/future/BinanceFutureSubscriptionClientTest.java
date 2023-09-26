package org.herman.future;

import org.herman.Constants;
import org.herman.exception.ApiException;
import org.herman.future.impl.WebSocketFutureSubscriptionClient;
import org.herman.future.impl.WebsocketRequestClient;
import org.herman.future.impl.binance.BinanceWebsocketRequestClient;
import org.herman.future.model.event.AggregateTradeEvent;
import org.junit.Test;

import static org.junit.Assert.*;

public class BinanceFutureSubscriptionClientTest {


    @Test
    public void subscribeAggregateTradeEvent() throws InterruptedException {

        FutureSubscriptionOptions options = new FutureSubscriptionOptions();
        options.setUri(Constants.Future.BINANCE_WS_API_BASE_URL);
        WebsocketRequestClient requestImpl = new BinanceWebsocketRequestClient();
        FutureSubscriptionClient subscriptionClient = new WebSocketFutureSubscriptionClient(options, requestImpl);


        subscriptionClient.subscribeAggregateTradeEvent("ETHUSDTM", new FutureSubscriptionListener<AggregateTradeEvent>() {
            @Override
            public void onReceive(AggregateTradeEvent data) {
                System.out.println(data);
            }
        }, new FutureSubscriptionErrorHandler() {
            @Override
            public void onError(ApiException exception) {
                exception.printStackTrace();
            }
        });

        Thread.sleep(60000 * 10);
    }


}