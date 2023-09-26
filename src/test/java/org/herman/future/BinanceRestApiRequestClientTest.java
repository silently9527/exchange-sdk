package org.herman.future;

import org.apache.commons.lang3.time.DateUtils;
import org.herman.future.FutureRequestClient;
import org.herman.future.impl.SyncFutureRequestClientImpl;
import org.herman.future.impl.binance.BinanceRestApiRequestClient;
import org.herman.future.model.enums.*;
import org.herman.future.model.market.Candlestick;
import org.herman.future.model.market.ExchangeInformation;
import org.herman.future.model.market.FundingRate;
import org.herman.future.model.market.MarkPrice;
import org.herman.future.model.trade.AccountBalance;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BinanceRestApiRequestClientTest {

    private String appKey = "rwuskEE2WtxPuG9cY9v0bHC0EVWsiz92chTWTY1m8lywFLF1AYbZFi2RWI33xV4C";
    private String secret = "cZbeVqjqN6wDkDpM2GdN899YDvEstzHT19t5PnwDbdvMUaUD9WvivPdh7lVdqCZg";

    @Test
    public void test() {
        BinanceRestApiRequestClient restApiRequestClient = new BinanceRestApiRequestClient(appKey, secret);
        FutureRequestClient client = new SyncFutureRequestClientImpl(restApiRequestClient);

//        ExchangeInformation exchangeInformation = client.getExchangeInformation();
//        System.out.println(exchangeInformation);

//        List<AccountBalance> balance = client.getBalance();
//        System.out.println(balance);

//        FundingRate fundingRate = client.getFundingRate("BTCUSDT");
//        System.out.println(fundingRate);
//
//        List<MarkPrice> markPrices = client.getMarkPrice("BTCUSDT");
//        System.out.println(markPrices);

        long start = DateUtils.addDays(new Date(), -1).getTime();
        long end = new Date().getTime();
//        List<Candlestick> candlesticks = client.getCandlestick("ETHUSDT", CandlestickInterval.EIGHT_HOURLY, start, end, 10);
//        System.out.println(candlesticks);

//        System.out.println(client.getFundingRateHistory("ETHUSDT", start, end, 100));


        String orderID = client.postOrder("ETHUSDT", OrderSide.SELL, PositionSide.BOTH, OrderType.LIMIT, TimeInForce.GTC,
                new BigDecimal("0.1"), new BigDecimal("2000"), false, null, null, null);
        System.out.println(orderID);

//        client.cancelOrder("ETHUSDT", orderID, null);
//        client.cancelAllOpenOrder("ETHUSDT");
        System.out.println(client.getOrder("ETHUSDT", orderID, ""));
    }


}