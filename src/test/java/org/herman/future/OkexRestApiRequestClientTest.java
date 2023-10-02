package org.herman.future;

import org.apache.commons.lang3.time.DateUtils;
import org.herman.Constants;
import org.herman.future.impl.FutureRestApiClientImpl;
import org.herman.future.impl.okex.OkexRestApiRequestClient;
import org.herman.future.model.market.*;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class OkexRestApiRequestClientTest {

    private String appKey = "6e843238-7f7a-42b9-b08f-94234dc03705";
    private String secret = "8128FCD3539DDC6052FA511F3F227A39";
    private String passphrase = "Zxc/.,321";

    @Test
    public void test() {
        FutureRestApiOptions options = new FutureRestApiOptions(Constants.Future.BINANCE_REST_API_BASE_URL, appKey, secret, passphrase);
        OkexRestApiRequestClient restApiRequestClient = new OkexRestApiRequestClient(options);
        FutureRestApiClient client = new FutureRestApiClientImpl(restApiRequestClient);

//        ExchangeInformation exchangeInformation = client.getExchangeInformation();
//        System.out.println(exchangeInformation);

//        ExchangeInformation exchangeInformation = client.getExchangeInformation();
//        System.out.println(exchangeInformation);

//        List<MarkPrice> markPrice = client.getMarkPrice("ETH-USDT-SWAP");
//        System.out.println(markPrice);

        long start = DateUtils.addDays(new Date(), -5).getTime();
        long end = new Date().getTime();
//        List<FundingRate> fundingRateHistory = client.getFundingRateHistory("TRB-USDT-SWAP", start, end, 10);
//        System.out.println(fundingRateHistory.size());
//        System.out.println(fundingRateHistory);

//        FundingRate fundingRate = client.getFundingRate("TRB-USDT-SWAP");
//        System.out.println(fundingRate);

//        List<Candlestick> candlestick = client.getCandlestick("ETH-USDT-SWAP", CandlestickInterval.ONE_MINUTE, start, end, 10);
//        System.out.println(candlestick.size());
//        System.out.println(candlestick);

//        List<AccountBalance> balance = client.getBalance();
//        System.out.println(balance);

//        2023-09-27T09:54:29.417ZPOST/api/v5/trade/order{"instId":"XRP-USDT-SWAP","tdMode":"cross","side":"BUY","posSide":"BOTH","ordType":"limit","sz":"1","px":"0.001","reduceOnly":"false"}
//        2023-09-27T09:53:40.109ZPOST/api/v5/trade/order{"instId":"XRP-USDT-SWAP","tdMode":"isolated","ccy":null,"clOrdId":null,"tag":null,"side":"buy","posSide":null,"ordType":"limit","sz":"1","px":"0.1","reduceOnly":null,"tgtCcy":null}
//        String orderId = client.postOrder("XRP-USDT-SWAP", OrderSide.BUY, PositionSide.BOTH, OrderType.LIMIT, TimeInForce.GTC, BigDecimal.ONE, new BigDecimal("0.1"), false, "", null, null);
//        System.out.println(orderId);
//
//        String orderId = "627516595312967702";
//        Order order = client.getOrder("XRP-USDT-SWAP", orderId, "");
//        System.out.println(orderId);

//        List<Order> openOrders = client.getOpenOrders("XRP-USDT-SWAP");
//        System.out.println(openOrders);

//        String orderId = client.cancelOrder("XRP-USDT-SWAP", "627268342415802372", "");
//        System.out.println(orderId);

//        ResponseResult responseResult = client.cancelAllOpenOrder("XRP-USDT-SWAP");
//        System.out.println(responseResult);

//        OrderBook orderBook = client.getOrderBook("ETH-USDT-SWAP", 10);
//        System.out.println(orderBook);

//        627516595312967702
//        List<Order> allOrders = client.getAllOrders("BTC-USD-SWAP", "", start, end, 2);
//        System.out.println(allOrders);

//        List<PositionRisk> positionRisk = client.getPositionRisk("BTC-USD-SWAP");
//        System.out.println(positionRisk);

//        List<Trade> recentTrades = client.getRecentTrades("BTC-USD-SWAP", 2);
//        System.out.println(recentTrades);


//        List<SymbolPrice> prices = client.getSymbolPriceTicker("ETH-USDT-SWAP");
//        System.out.println(prices);
//        prices = client.getSymbolPriceTicker("");
//        System.out.println(prices);


        List<SymbolOrderBook> orderBookTicker = client.getSymbolOrderBookTicker("ETH-USDT-SWAP");
        System.out.println(orderBookTicker);
        orderBookTicker = client.getSymbolOrderBookTicker("");
        System.out.println(orderBookTicker);
    }

}