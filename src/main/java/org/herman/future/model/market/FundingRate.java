package org.herman.future.model.market;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.herman.Constants;

import java.math.BigDecimal;

public class FundingRate {

    private String symbol;

    private BigDecimal fundingRate;

    private Long fundingTime;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getFundingRate() {
        return fundingRate;
    }

    public void setFundingRate(BigDecimal fundingRate) {
        this.fundingRate = fundingRate;
    }

    public Long getFundingTime() {
        return fundingTime;
    }

    public void setFundingTime(Long fundingTime) {
        this.fundingTime = fundingTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, Constants.TO_STRING_BUILDER_STYLE).append("symbol", symbol)
                .append("fundingRate", fundingRate).append("fundingTime", fundingTime).toString();
    }
}
