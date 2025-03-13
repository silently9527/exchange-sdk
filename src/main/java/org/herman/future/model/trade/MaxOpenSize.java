package org.herman.future.model.trade;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.herman.Constants;

public class MaxOpenSize {
    private String symbol;
    private Integer maxBuyOpenSize;
    private Integer maxSellOpenSize;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getMaxBuyOpenSize() {
        return maxBuyOpenSize;
    }

    public void setMaxBuyOpenSize(Integer maxBuyOpenSize) {
        this.maxBuyOpenSize = maxBuyOpenSize;
    }

    public Integer getMaxSellOpenSize() {
        return maxSellOpenSize;
    }

    public void setMaxSellOpenSize(Integer maxSellOpenSize) {
        this.maxSellOpenSize = maxSellOpenSize;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, Constants.TO_STRING_BUILDER_STYLE).append("symbol", symbol)
                .append("maxBuyOpenSize", maxBuyOpenSize).append("maxSellOpenSize", maxSellOpenSize).toString();
    }
}
