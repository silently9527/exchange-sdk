package org.herman.future;

import org.apache.commons.lang3.time.DateUtils;
import org.herman.future.FutureRequestClient;
import org.herman.future.impl.SyncFutureRequestClientImpl;
import org.herman.future.impl.binance.BinanceRestApiRequestClient;
import org.herman.future.model.enums.*;
import org.herman.future.model.market.*;
import org.herman.future.model.trade.AccountBalance;
import org.herman.future.model.trade.Order;
import org.herman.future.model.trade.PositionRisk;
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

        long start = DateUtils.addDays(new Date(), -5).getTime();
        long end = new Date().getTime();
//        List<Candlestick> candlesticks = client.getCandlestick("ETHUSDT", CandlestickInterval.EIGHT_HOURLY, start, end, 10);
//        System.out.println(candlesticks);

//        System.out.println(client.getFundingRateHistory("ETHUSDT", start, end, 100));


//        String orderID = client.postOrder("ETHUSDT", OrderSide.SELL, PositionSide.BOTH, OrderType.LIMIT, TimeInForce.GTC,
//                new BigDecimal("0.1"), new BigDecimal("2000"), false, null, null, null);
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

        List<AggregateTrade> aggregateTrades = client.getAggregateTrades("ETHUSDT", "", start, end, 10);
        System.out.println(aggregateTrades);
    }


}