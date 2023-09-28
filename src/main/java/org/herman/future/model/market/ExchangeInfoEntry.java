package org.herman.future.model.market;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.herman.Constants;
import org.herman.future.model.enums.FutureStatus;
import org.herman.future.model.enums.FutureType;

import java.math.BigDecimal;

public class ExchangeInfoEntry {

    private String symbol;
    private String baseAsset;           // 标的资产
    private String quoteAsset;          // 报价资产
    private Long onboardDate;           // 上线日期
    private FutureStatus status;
    private FutureType futureType;
    private BigDecimal maxPrice;        // 价格上限, 最大价格
    private BigDecimal minPrice;        // 价格下限, 最小价格
    private BigDecimal tickSize;        // 订单最小价格间隔
    private BigDecimal maxQty;          // 数量上限, 最大数量
    private BigDecimal minQty;          // 数量下限, 最小数量
    private BigDecimal stepSize;        // 订单最小数量间隔
    private Integer maxNumOrders;       // 最多订单数限制
    private BigDecimal minNotional;     // 最小名义价值
    private BigDecimal multiplier;

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public Long getOnboardDate() {
        return onboardDate;
    }

    public void setOnboardDate(Long onboardDate) {
        this.onboardDate = onboardDate;
    }

    public BigDecimal getStepSize() {
        return stepSize;
    }

    public void setStepSize(BigDecimal stepSize) {
        this.stepSize = stepSize;
    }

    public FutureType getFutureType() {
        return futureType;
    }

    public void setFutureType(FutureType futureType) {
        this.futureType = futureType;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public FutureStatus getStatus() {
        return status;
    }

    public void setStatus(FutureStatus status) {
        this.status = status;
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getTickSize() {
        return tickSize;
    }

    public void setTickSize(BigDecimal tickSize) {
        this.tickSize = tickSize;
    }

    public BigDecimal getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(BigDecimal maxQty) {
        this.maxQty = maxQty;
    }

    public BigDecimal getMinQty() {
        return minQty;
    }

    public void setMinQty(BigDecimal minQty) {
        this.minQty = minQty;
    }

    public Integer getMaxNumOrders() {
        return maxNumOrders;
    }

    public void setMaxNumOrders(Integer maxNumOrders) {
        this.maxNumOrders = maxNumOrders;
    }

    public BigDecimal getMinNotional() {
        return minNotional;
    }

    public void setMinNotional(BigDecimal minNotional) {
        this.minNotional = minNotional;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, Constants.TO_STRING_BUILDER_STYLE)
                .append("symbol", symbol)
                .append("status", status)
                .append("baseAsset", baseAsset)
                .append("quoteAsset", quoteAsset)
                .append("onboardDate", onboardDate)
                .append("maxPrice", maxPrice)
                .append("minPrice", minPrice)
                .append("tickSize", tickSize)
                .append("minNotional", minNotional)
                .append("minQty", minQty)
                .append("maxQty", maxQty)
                .toString();
    }
}
