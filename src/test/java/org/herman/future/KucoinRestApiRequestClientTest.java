package org.herman.future;

import org.apache.commons.lang3.time.DateUtils;
import org.herman.Constants;
import org.herman.future.model.enums.OrderSide;
import org.herman.future.model.enums.OrderType;
import org.herman.future.model.enums.PositionSide;
import org.herman.future.model.enums.TimeInForce;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

public class KucoinRestApiRequestClientTest {

    private String appKey = "65006f4522d1690001b0285d";
    private String secret = "80feb39e-82d4-46c2-97c8-7fa2fe90290a";
    private String passphrase = "1588452";

    @Test
    public void test() {
        FutureRestApiClient client = FutureApiInternalFactory
                .getInstance()
                .createKucoinFutureRestApiClient(Constants.Future.KUCOIN_REST_API_BASE_URL, appKey, secret, passphrase);


        long start = DateUtils.addDays(new Date(), -1).getTime();
        long end = new Date().getTime();
//        System.out.println(client.getMarkPrice("ETHUSDTM"));
//        System.out.println(client.getFutures());
        System.out.println(client.getFuture("ETHUSDTM"));
//        System.out.println(client.getFundingRate("ETHUSDTM"));
//        System.out.println(client.getCandlestick("ETHUSDTM", CandlestickInterval.FIVE_MINUTES, start, end, 10));
//        System.out.println(client.getBalance());
//        System.out.println(client.getOrderBook("ETHUSDTM", 10));

//        System.out.println(client.getSymbolPriceTicker("ETHUSDTM"));
//        System.out.println(client.getSymbolOrderBookTicker("ETHUSDTM"));
//        System.out.println(client.getRecentTrades("ETHUSDTM", 10));

//        System.out.println(client.getPositionRisk(""));
//        System.out.println(client.getPositionRisk("ETHUSDTM"));

//        System.out.println(client.getOpenOrders("ETHUSDTM"));
//        System.out.println(client.getAllOrders("ETHUSDTM", "1", start, end, 2));
//        99410585954889728  99066920745111552
//        System.out.println(client.getOrder("","99410585954889728",""));

//        99417981246783488
//        String orderId = client.postOrder("ETHUSDTM", OrderSide.SELL, PositionSide.BOTH, OrderType.MARKET,
//                TimeInForce.GTC, BigDecimal.ONE, null, false, System.currentTimeMillis() + "", null, null, 5);
//        System.out.println(client.getOrder("ETHUSDTM", orderId, null));
//        System.out.println(client.getOrder("ETHUSDTM", "100418879355908097", null));

//        System.out.println(client.cancelOrder("ETHUSDTM", orderId, ""));
//        System.out.println(client.cancelAllOpenOrder("ETHUSDTM"));


    }


}