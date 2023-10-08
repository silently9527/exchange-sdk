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

public class BinanceRestApiRequestClientTest {

    private String appKey = "rwuskEE2WtxPuG9cY9v0bHC0EVWsiz92chTWTY1m8lywFLF1AYbZFi2RWI33xV4C";
    private String secret = "cZbeVqjqN6wDkDpM2GdN899YDvEstzHT19t5PnwDbdvMUaUD9WvivPdh7lVdqCZg";

    @Test
    public void test() {
        FutureRestApiClient client = FutureApiInternalFactory
                .getInstance()
                .createBinanceFutureRestApiClient(Constants.Future.BINANCE_REST_API_BASE_URL, appKey, secret);

//        System.out.println(client.getFutures());
//        System.out.println(client.getFuture("ETHUSDT"));

//        List<AccountBalance> balance = client.getBalance();
//        System.out.println(balance);

//        FundingRate fundingRate = client.getFundingRate("BTCUSDT");
//        System.out.println(fundingRate);
//
//        List<MarkPrice> markPrices = client.getMarkPrice("BTCUSDT");
//        System.out.println(markPrices);

        long start = DateUtils.addDays(new Date(), -5).getTime();
        long end = new Date().getTime();
//        List<Candlestick> candlesticks = client.getCandlestick("ETHUSDT", CandlestickInterval.EIGHT_HOURLY, start, end, 10);
//        System.out.println(candlesticks);

//        System.out.println(client.getFundingRateHistory("ETHUSDT", start, end, 100));


//        String orderID = client.postOrder("ETHUSDT", OrderSide.SELL, PositionSide.BOTH, OrderType.MARKET, TimeInForce.GTC,
//                new BigDecimal("0.01"), new BigDecimal("2000"), false, null, null, null, 5);
//        System.out.println(orderID);

//        client.cancelOrder("ETHUSDT", orderID, null);
//        client.cancelAllOpenOrder("ETHUSDT");
//        System.out.println(client.getOrder("ETHUSDT", orderID, ""));


//        OrderBook orderBook = client.getOrderBook("ETHUSDT", 5);
//        System.out.println(orderBook);

//        List<Order> orders = client.getAllOrders("BTCUSDT", "", start, end, 10);
//        System.out.println(orders);

//        List<PositionRisk> positionRisk = client.getPositionRisk("BTCUSDT");
//        System.out.println(positionRisk);

//        List<Trade> trades = client.getRecentTrades("BTCUSDT", 2);
//        System.out.println(trades);

//        List<SymbolPrice> prices = client.getSymbolPriceTicker("ETHUSDT");
//        System.out.println(prices);
//        prices = client.getSymbolPriceTicker("");
//        System.out.println(prices);

//        List<SymbolOrderBook> orderBookTicker = client.getSymbolOrderBookTicker("ETHUSDT");
//        System.out.println(orderBookTicker);
//        orderBookTicker = client.getSymbolOrderBookTicker("");
//        System.out.println(orderBookTicker);

//        List<AggregateTrade> aggregateTrades = client.getAggregateTrades("ETHUSDT", "", start, end, 10);
//        System.out.println(aggregateTrades);
    }


}