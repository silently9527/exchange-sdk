package org.herman.future;


import org.herman.future.model.ResponseResult;
import org.herman.future.model.enums.*;
import org.herman.future.model.market.*;
import org.herman.future.model.trade.*;

import java.math.BigDecimal;
import java.util.List;

public interface FutureRestApiClient {

    ExchangeInformation getExchangeInformation();

    OrderBook getOrderBook(String symbol, Integer limit);

    List<Trade> getRecentTrades(String symbol, Integer limit);

//    List<Trade> getOldTrades(String symbol, Integer limit, Long fromId);

    List<AggregateTrade> getAggregateTrades(String symbol, String fromId, Long startTime, Long endTime, Integer limit);

    List<Candlestick> getCandlestick(String symbol, CandlestickInterval interval, Long startTime, Long endTime, Integer limit);

    List<MarkPrice> getMarkPrice(String symbol);

    List<FundingRate> getFundingRateHistory(String symbol, Long startTime, Long endTime, Integer limit);

    FundingRate getFundingRate(String symbol);


    //    List<PriceChangeTicker> get24hrTickerPriceChange(String symbol);
    //最新价格
    List<SymbolPrice> getSymbolPriceTicker(String symbol);

    //当前最优挂单
    List<SymbolOrderBook> getSymbolOrderBookTicker(String symbol);


//    List<LiquidationOrder> getLiquidationOrders(String symbol, Long startTime, Long endTime, Integer limit);

//    List<Object> postBatchOrders(String batchOrders);

    String postOrder(String symbol, OrderSide side, PositionSide positionSide, OrderType orderType,
                     TimeInForce timeInForce, BigDecimal quantity, BigDecimal price, Boolean reduceOnly,
                     String newClientOrderId, BigDecimal stopPrice, WorkingType workingType, Integer leverage);

    String cancelOrder(String symbol, String orderId, String origClientOrderId);

    ResponseResult cancelAllOpenOrder(String symbol);

//    ResponseResult changePositionSide(boolean dual);

    //    ResponseResult changeMarginType(String symbolName, String marginType);
//
//    JSONObject addIsolatedPositionMargin(String symbolName, int type, String amount, PositionSide positionSide);
//
//    List<WalletDeltaLog> getPositionMarginHistory(String symbolName, int type, long startTime, long endTime, int limit);
//
//    JSONObject getPositionSide();
//
    Order getOrder(String symbol, String orderId, String origClientOrderId);

    List<Order> getOpenOrders(String symbol);

    List<Order> getAllOrders(String symbol, String formId, Long startTime, Long endTime, Integer limit);

    List<AccountBalance> getBalance();

    AccountInformation getAccountInformation();

    Leverage changeInitialLeverage(String symbol, Integer leverage);

    List<PositionRisk> getPositionRisk(String symbol);

//    List<MyTrade> getAccountTrades(String symbol, Long startTime, Long endTime, Long fromId, Integer limit);

}