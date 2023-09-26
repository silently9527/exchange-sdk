package org.herman.future.model.market;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.herman.Constants;

import java.math.BigDecimal;

public class MarkPrice {

    private String symbol;

    private BigDecimal markPrice;

    private Long time;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getMarkPrice() {
        return markPrice;
    }

    public void setMarkPrice(BigDecimal markPrice) {
        this.markPrice = markPrice;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, Constants.TO_STRING_BUILDER_STYLE).append("symbol", symbol)
                .append("markPrice", markPrice)
                .append("time", time).toString();
    }
}