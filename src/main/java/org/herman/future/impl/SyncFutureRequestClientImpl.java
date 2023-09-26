package org.herman.future.impl;

import org.herman.future.FutureRequestClient;
import org.herman.future.RestApiInvoker;
import org.herman.future.model.ResponseResult;
import org.herman.future.model.enums.*;
import org.herman.future.model.market.Candlestick;
import org.herman.future.model.market.ExchangeInformation;
import org.herman.future.model.market.FundingRate;
import org.herman.future.model.market.MarkPrice;
import org.herman.future.model.trade.*;

import java.math.BigDecimal;
import java.util.List;

public class SyncFutureRequestClientImpl implements FutureRequestClient {

    private final RestApiRequestClient requestImpl;

    public SyncFutureRequestClientImpl(RestApiRequestClient restApiRequestClient) {
        this.requestImpl = restApiRequestClient;
    }


    @Override
    public ExchangeInformation getExchangeInformation() {
        return RestApiInvoker.callSync(requestImpl.getExchangeInformation());
    }

    @Override
    public List<Candlestick> getCandlestick(String symbol, CandlestickInterval interval, Long startTime, Long endTime, Integer limit) {
        return RestApiInvoker.callSync(requestImpl.getCandlestick(symbol, interval, startTime, endTime, limit));
    }

    @Override
    public List<MarkPrice> getMarkPrice(String symbol) {
        return RestApiInvoker.callSync(requestImpl.getMarkPrice(symbol));
    }

    @Override
    public List<FundingRate> getFundingRateHistory(String symbol, Long startTime, Long endTime, Integer limit) {
        return RestApiInvoker.callSync(requestImpl.getFundingRateHistory(symbol, startTime, endTime, limit));
    }

    @Override
    public FundingRate getFundingRate(String symbol) {
        return RestApiInvoker.callSync(requestImpl.getFundingRate(symbol));
    }

    @Override
    public String postOrder(String symbol, OrderSide side, PositionSide positionSide, OrderType orderType, TimeInForce timeInForce, BigDecimal quantity, BigDecimal price, Boolean reduceOnly, String newClientOrderId, BigDecimal stopPrice, WorkingType workingType) {
        return RestApiInvoker.callSync(requestImpl.postOrder(symbol, side, positionSide, orderType,
                timeInForce, quantity, price, reduceOnly,
                newClientOrderId, stopPrice, workingType));
    }

    @Override
    public String cancelOrder(String symbol, String orderId, String origClientOrderId) {
        return RestApiInvoker.callSync(requestImpl.cancelOrder(symbol, orderId, origClientOrderId));
    }

    @Override
    public ResponseResult cancelAllOpenOrder(String symbol) {
        return RestApiInvoker.callSync(requestImpl.cancelAllOpenOrder(symbol));
    }

    @Override
    public Order getOrder(String symbol, String orderId, String origClientOrderId) {
        return RestApiInvoker.callSync(requestImpl.getOrder(symbol, orderId, origClientOrderId));
    }

    @Override
    public List<Order> getOpenOrders(String symbol) {
        return RestApiInvoker.callSync(requestImpl.getOpenOrders(symbol));
    }

    @Override
    public List<AccountBalance> getBalance() {
        return RestApiInvoker.callSync(requestImpl.getBalance());
    }

    @Override
    public AccountInformation getAccountInformation() {
        return RestApiInvoker.callSync(requestImpl.getAccountInformation());
    }

    @Override
    public Leverage changeInitialLeverage(String symbol, Integer leverage) {
        return RestApiInvoker.callSync(requestImpl.changeInitialLeverage(symbol,leverage));
    }

    @Override
    public List<PositionRisk> getPositionRisk() {
        return null;
    }


}
