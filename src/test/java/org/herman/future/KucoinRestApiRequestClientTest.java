package org.herman.future;

import org.apache.commons.lang3.time.DateUtils;
import org.herman.Constants;
import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.market.AggregateTrade;
import org.herman.future.model.market.MarkPrice;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class KucoinRestApiRequestClientTest {

    private String appKey = "65006f4522d1690001b0285d";
    private String secret = "80feb39e-82d4-46c2-97c8-7fa2fe90290a";
    private String passphrase = "1588452";

    @Test
    public void test() {
        FutureRestApiOptions options = new FutureRestApiOptions(Constants.Future.KUCOIN_REST_API_BASE_URL, appKey, secret, passphrase);
        FutureRestApiClient client = FutureApiInternalFactory.getInstance().createKucoinFutureRequestClient(options);


        long start = DateUtils.addDays(new Date(), -1).getTime();
        long end = new Date().getTime();
//        System.out.println(client.getMarkPrice("ETHUSDTM"));
//        System.out.println(client.getExchangeInformation());
//        System.out.println(client.getFundingRate("ETHUSDTM"));
//        System.out.println(client.getCandlestick("ETHUSDTM", CandlestickInterval.FIVE_MINUTES, start, end, 10));
//        System.out.println(client.getBalance());
//        System.out.println(client.getOrderBook("ETHUSDTM", 10));

//        System.out.println(client.getSymbolPriceTicker("ETHUSDTM"));
//        System.out.println(client.getSymbolOrderBookTicker("ETHUSDTM"));
//        System.out.println(client.getRecentTrades("ETHUSDTM", 10));

//        System.out.println(client.getPositionRisk(""));
        System.out.println(client.getPositionRisk("ETHUSDTM"));


    }


}