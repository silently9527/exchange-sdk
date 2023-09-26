package org.herman.future;

import org.herman.future.impl.SyncFutureRequestClientImpl;
import org.herman.future.impl.binance.BinanceRestApiRequestClient;
import org.herman.future.impl.okex.OkexRestApiRequestClient;
import org.herman.future.model.market.ExchangeInformation;
import org.junit.Test;

public class OkexRestApiRequestClientTest {

    private String appKey = "rwuskEE2WtxPuG9cY9v0bHC0EVWsiz92chTWTY1m8lywFLF1AYbZFi2RWI33xV4C";
    private String secret = "cZbeVqjqN6wDkDpM2GdN899YDvEstzHT19t5PnwDbdvMUaUD9WvivPdh7lVdqCZg";

    @Test
    public void test() {
        OkexRestApiRequestClient restApiRequestClient = new OkexRestApiRequestClient(appKey, secret);
        FutureRequestClient client = new SyncFutureRequestClientImpl(restApiRequestClient);

//        ExchangeInformation exchangeInformation = client.getExchangeInformation();
//        System.out.println(exchangeInformation);

        ExchangeInformation exchangeInformation = client.getExchangeInformation();
        System.out.println(exchangeInformation);
    }

}