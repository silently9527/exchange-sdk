package org.herman.future;


import com.alibaba.fastjson.JSONObject;
import org.herman.future.model.ResponseResult;
import org.herman.future.model.enums.*;
import org.herman.future.model.market.Candlestick;
import org.herman.future.model.market.ExchangeInformation;
import org.herman.future.model.market.FundingRate;
import org.herman.future.model.market.MarkPrice;
import org.herman.future.model.trade.*;

import java.math.BigDecimal;
import java.util.List;

public interface FutureRequestClient {

    ExchangeInformation getExchangeInformation();


//    OrderBook getOrderBook(String symbol, Integer limit);
//
//    List<Trade> getRecentTrades(String symbol, Integer limit);
//
//    List<Trade> getOldTrades(String symbol, Integer limit, Long fromId);
//
//    List<AggregateTrade> getAggregateTrades(String symbol, Long fromId, Long startTime, Long endTime, Integer limit);
//
    List<Candlestick> getCandlestick(String symbol, CandlestickInterval interval, Long startTime, Long endTime, Integer limit);

    List<MarkPrice> getMarkPrice(String symbol);

    List<FundingRate> getFundingRateHistory(String symbol, Long startTime, Long endTime, Integer limit);

    FundingRate getFundingRate(String symbol);
//
//    List<PriceChangeTicker> get24hrTickerPriceChange(String symbol);
//
//    List<SymbolPrice> getSymbolPriceTicker(String symbol);
//
//    List<SymbolOrderBook> getSymbolOrderBookTicker(String symbol);
//
//    List<LiquidationOrder> getLiquidationOrders(String symbol, Long startTime, Long endTime, Integer limit);
//
//    List<Object> postBatchOrders(String batchOrders);
//
    String postOrder(String symbol, OrderSide side, PositionSide positionSide, OrderType orderType,
                     TimeInForce timeInForce, BigDecimal quantity, BigDecimal price, Boolean reduceOnly,
                     String newClientOrderId, BigDecimal stopPrice, WorkingType workingType);

    String cancelOrder(String symbol, String orderId, String origClientOrderId);

    ResponseResult cancelAllOpenOrder(String symbol);

//    ResponseResult changePositionSide(boolean dual);
//
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
//
//    List<Order> getAllOrders(String symbol, String orderId, Long startTime, Long endTime, Integer limit);
//
    List<AccountBalance> getBalance();

    AccountInformation getAccountInformation();

    Leverage changeInitialLeverage(String symbol, Integer leverage);

    List<PositionRisk> getPositionRisk();

//    List<MyTrade> getAccountTrades(String symbol, Long startTime, Long endTime, Long fromId, Integer limit);

}