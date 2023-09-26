package org.herman.future.impl;

import org.herman.future.model.ResponseResult;
import org.herman.future.model.enums.*;
import org.herman.future.model.market.Candlestick;
import org.herman.future.model.market.ExchangeInformation;
import org.herman.future.model.market.FundingRate;
import org.herman.future.model.market.MarkPrice;
import org.herman.future.model.trade.AccountBalance;
import org.herman.future.model.trade.AccountInformation;
import org.herman.future.model.trade.Leverage;
import org.herman.future.model.trade.Order;

import java.math.BigDecimal;
import java.util.List;

public interface RestApiRequestClient {
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
}
