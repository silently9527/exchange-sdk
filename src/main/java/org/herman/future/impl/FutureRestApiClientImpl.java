package org.herman.future.impl;

import org.herman.future.FutureRestApiClient;
import org.herman.future.RestApiInvoker;
import org.herman.future.model.ResponseResult;
import org.herman.future.model.enums.*;
import org.herman.future.model.market.*;
import org.herman.future.model.trade.*;

import java.math.BigDecimal;
import java.util.List;

public class FutureRestApiClientImpl implements FutureRestApiClient {

    private final RestApiRequestClient requestImpl;

    public FutureRestApiClientImpl(RestApiRequestClient restApiRequestClient) {
        this.requestImpl = restApiRequestClient;
    }

    @Override
    public List<Future> getFutures() {
        return RestApiInvoker.callSync(requestImpl.getFutures());
    }

    @Override
    public Future getFuture(String symbol) {
        return RestApiInvoker.callSync(requestImpl.getFuture(symbol));
    }

    @Override
    public OrderBook getOrderBook(String symbol, Integer limit) {
        return RestApiInvoker.callSync(requestImpl.getOrderBook(symbol, limit));
    }

    @Override
    public List<Trade> getRecentTrades(String symbol, Integer limit) {
        return RestApiInvoker.callSync(requestImpl.getRecentTrades(symbol, limit));
    }

    @Override
    public List<AggregateTrade> getAggregateTrades(String symbol, String fromId, Long startTime, Long endTime, Integer limit) {
        return RestApiInvoker.callSync(requestImpl.getAggregateTrades(symbol, fromId, startTime, endTime, limit));
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
    public SymbolPrice getSymbolPriceTicker(String symbol) {
        return RestApiInvoker.callSync(requestImpl.getSymbolPriceTicker(symbol)).get(0);
    }

    @Override
    public SymbolOrderBook getSymbolOrderBookTicker(String symbol) {
        return RestApiInvoker.callSync(requestImpl.getSymbolOrderBookTicker(symbol)).get(0);
    }

    @Override
    public String postOrder(String symbol, OrderSide side, PositionSide positionSide, OrderType orderType, TimeInForce timeInForce,
                            BigDecimal quantity, BigDecimal price, Boolean reduceOnly, String newClientOrderId,
                            BigDecimal stopPrice, WorkingType workingType, Integer leverage) {
        return RestApiInvoker.callSync(requestImpl.postOrder(symbol, side, positionSide, orderType,
                timeInForce, quantity, price, reduceOnly,
                newClientOrderId, stopPrice, workingType, leverage));
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
    public List<Order> getAllOrders(String symbol, String formId, Long startTime, Long endTime, Integer limit) {
        return RestApiInvoker.callSync(requestImpl.getAllOrders(symbol, formId, startTime, endTime, limit));
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
        return RestApiInvoker.callSync(requestImpl.changeInitialLeverage(symbol, leverage));
    }

    @Override
    public PositionRisk getPositionRisk(String symbol) {
        return RestApiInvoker.callSync(requestImpl.getPositionRisk(symbol)).get(0);
    }


}
