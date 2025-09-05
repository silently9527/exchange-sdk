package org.herman.future.model.market;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.herman.Constants;

import java.math.BigDecimal;

public class FundingInfo {

    private String symbol;
    private BigDecimal fundingRateCap;
    private BigDecimal fundingRateFloor;
    private Integer fundingIntervalHours = 8;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    public Integer getFundingIntervalHours() {
        return fundingIntervalHours;
    }

    public void setFundingIntervalHours(Integer fundingIntervalHours) {
        this.fundingIntervalHours = fundingIntervalHours;
    }

    public BigDecimal getFundingRateCap() {
        return fundingRateCap;
    }

    public void setFundingRateCap(BigDecimal fundingRateCap) {
        this.fundingRateCap = fundingRateCap;
    }

    public BigDecimal getFundingRateFloor() {
        return fundingRateFloor;
    }

    public void setFundingRateFloor(BigDecimal fundingRateFloor) {
        this.fundingRateFloor = fundingRateFloor;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, Constants.TO_STRING_BUILDER_STYLE)
                .append("symbol", symbol)
                .append("fundingIntervalHours", fundingIntervalHours)
                .append("fundingRateCap", fundingRateCap)
                .append("fundingRateFloor", fundingRateFloor)
                .toString();
    }
}
