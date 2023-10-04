package org.herman.future.impl;

import org.herman.future.model.ResponseResult;
import org.herman.future.model.enums.*;
import org.herman.future.model.market.*;
import org.herman.future.model.trade.*;

import java.math.BigDecimal;
import java.util.List;

public interface RestApiRequestClient {
    //todo:缓存
    RestApiRequest<ExchangeInformation> getExchangeInformation();

    RestApiRequest<List<MarkPrice>> getMarkPrice(String symbol);

    RestApiRequest<List<FundingRate>> getFundingRateHistory(String symbol, Long startTime, Long endTime, Integer limit);

    RestApiRequest<FundingRate> getFundingRate(String symbol);

    RestApiRequest<List<Candlestick>> getCandlestick(String symbol, CandlestickInterval interval, Long startTime, Long endTime, Integer limit);

    RestApiRequest<AccountInformation> getAccountInformation();

    RestApiRequest<List<AccountBalance>> getBalance();

    RestApiRequest<String> postOrder(String symbol, OrderSide side, PositionSide positionSide, OrderType orderType, TimeInForce timeInForce, BigDecimal quantity, BigDecimal price, Boolean reduceOnly, String newClientOrderId, BigDecimal stopPrice, WorkingType workingType);

    RestApiRequest<String> cancelOrder(String symbol, String orderId, String origClientOrderId);

    RestApiRequest<ResponseResult> cancelAllOpenOrder(String symbol);

    RestApiRequest<Order> getOrder(String symbol, String orderId, String origClientOrderId);

    RestApiRequest<List<Order>> getOpenOrders(String symbol);

    RestApiRequest<Leverage> changeInitialLeverage(String symbol, Integer leverage);

    RestApiRequest<OrderBook> getOrderBook(String symbol, Integer limit);

    RestApiRequest<List<Order>> getAllOrders(String symbol, String formId, Long startTime, Long endTime, Integer limit);

    RestApiRequest<List<PositionRisk>> getPositionRisk(String symbol);

    RestApiRequest<List<Trade>> getRecentTrades(String symbol, Integer limit);

    RestApiRequest<List<SymbolPrice>> getSymbolPriceTicker(String symbol);

    RestApiRequest<List<SymbolOrderBook>> getSymbolOrderBookTicker(String symbol);

    RestApiRequest<List<AggregateTrade>> getAggregateTrades(String symbol, String fromId, Long startTime, Long endTime, Integer limit);
}
