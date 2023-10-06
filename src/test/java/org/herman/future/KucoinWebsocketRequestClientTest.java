package org.herman.future;

import org.herman.Constants;
import org.junit.Test;

public class KucoinWebsocketRequestClientTest {

    private String appKey = "65006f4522d1690001b0285d";
    private String secret = "80feb39e-82d4-46c2-97c8-7fa2fe90290a";
    private String passphrase = "1588452";

    @Test
    public void test() throws InterruptedException {
        FutureSubscriptionClient client = FutureApiInternalFactory
                .getInstance()
                .createKucoinFutureSubscriptionClient(Constants.Future.KUCOIN_REST_API_BASE_URL, appKey, secret, passphrase, false);

//        client.subscribeMarkPriceEvent("ETHUSDTM", System.out::println, null);


        client = FutureApiInternalFactory
                .getInstance()
                .createKucoinFutureSubscriptionClient(Constants.Future.KUCOIN_REST_API_BASE_URL, appKey, secret, passphrase, true);

        client.subscribeOrderUpdateEvent("ETHUSDTM", System.out::println, null);
        Thread.sleep(60000 * 10);
    }


}