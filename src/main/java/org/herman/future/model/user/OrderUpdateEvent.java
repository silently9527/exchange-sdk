package org.herman.future.model.user;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.herman.Constants;
import org.herman.future.model.enums.OrderSide;
import org.herman.future.model.enums.OrderStatus;
import org.herman.future.model.enums.OrderType;

import java.math.BigDecimal;

public class OrderUpdateEvent {

    private String symbol;

    private String clientOrderId;

    private OrderSide side;

    private OrderType type;

    private String timeInForce;

    private BigDecimal origQty;

    private BigDecimal price;

    private BigDecimal avgPrice;

    private BigDecimal stopPrice;

    private String executionType;

    private OrderStatus status;

    private Long orderId;

    private BigDecimal lastFilledQty;

    private BigDecimal cumulativeFilledQty;

    private BigDecimal lastFilledPrice;

    private String commissionAsset;

    private BigDecimal commissionAmount;

    private Long orderTradeTime;

    private Long tradeId;

    private BigDecimal bidsNotional;

    private BigDecimal asksNotional;

    private Boolean isMarkerSide;

    private Boolean isReduceOnly;

    private String workingType;

    private BigDecimal activationPrice;

    private BigDecimal callbackRate;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public OrderSide getSide() {
        return side;
    }

    public void setSide(OrderSide side) {
        this.side = side;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public BigDecimal getOrigQty() {
        return origQty;
    }

    public void setOrigQty(BigDecimal origQty) {
        this.origQty = origQty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(BigDecimal avgPrice) {
        this.avgPrice = avgPrice;
    }

    public BigDecimal getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(BigDecimal stopPrice) {
        this.stopPrice = stopPrice;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getLastFilledQty() {
        return lastFilledQty;
    }

    public void setLastFilledQty(BigDecimal lastFilledQty) {
        this.lastFilledQty = lastFilledQty;
    }

    public BigDecimal getCumulativeFilledQty() {
        return cumulativeFilledQty;
    }

    public void setCumulativeFilledQty(BigDecimal cumulativeFilledQty) {
        this.cumulativeFilledQty = cumulativeFilledQty;
    }

    public BigDecimal getLastFilledPrice() {
        return lastFilledPrice;
    }

    public void setLastFilledPrice(BigDecimal lastFilledPrice) {
        this.lastFilledPrice = lastFilledPrice;
    }

    public String getCommissionAsset() {
        return commissionAsset;
    }

    public void setCommissionAsset(String commissionAsset) {
        this.commissionAsset = commissionAsset;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public Long getOrderTradeTime() {
        return orderTradeTime;
    }

    public void setOrderTradeTime(Long orderTradeTime) {
        this.orderTradeTime = orderTradeTime;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public BigDecimal getBidsNotional() {
        return bidsNotional;
    }

    public void setBidsNotional(BigDecimal bidsNotional) {
        this.bidsNotional = bidsNotional;
    }

    public BigDecimal getAsksNotional() {
        return asksNotional;
    }

    public void setAsksNotional(BigDecimal asksNotional) {
        this.asksNotional = asksNotional;
    }

    public Boolean getIsMarkerSide() {
        return isMarkerSide;
    }

    public void setIsMarkerSide(Boolean isMarkerSide) {
        this.isMarkerSide = isMarkerSide;
    }

    public Boolean getIsReduceOnly() {
        return isReduceOnly;
    }

    public void setIsReduceOnly(Boolean isReduceOnly) {
        this.isReduceOnly = isReduceOnly;
    }

    public String getWorkingType() {
        return workingType;
    }

    public void setWorkingType(String workingType) {
        this.workingType = workingType;
    }

    public BigDecimal getActivationPrice() {
        return activationPrice;
    }

    public void setActivationPrice(BigDecimal activationPrice) {
        this.activationPrice = activationPrice;
    }

    public BigDecimal getCallbackRate() {
        return callbackRate;
    }

    public void setCallbackRate(BigDecimal callbackRate) {
        this.callbackRate = callbackRate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, Constants.TO_STRING_BUILDER_STYLE).append("symbol", symbol)
                .append("clientOrderId", clientOrderId).append("side", side).append("type", type)
                .append("timeInForce", timeInForce).append("origQty", origQty).append("price", price)
                .append("avgPrice", avgPrice).append("stopPrice", stopPrice).append("executionType", executionType)
                .append("orderStatus", status).append("orderId", orderId).append("lastFilledQty", lastFilledQty)
                .append("cumulativeFilledQty", cumulativeFilledQty).append("lastFilledPrice", lastFilledPrice)
                .append("commissionAsset", commissionAsset).append("commissionAmount", commissionAmount)
                .append("orderTradeTime", orderTradeTime).append("tradeId", tradeId)
                .append("bidsNotional", bidsNotional).append("asksNotional", asksNotional)
                .append("isMarkerSide", isMarkerSide).append("isReduceOnly", isReduceOnly)
                .append("workingType", workingType).append("activationPrice", activationPrice)
                .append("callbackRate", callbackRate).toString();
    }
}
