package org.herman.future.model.trade;

import java.math.BigDecimal;


public class LeverageBracket {
    private int bracket; // 层级
    private int initialLeverage; // 该层允许的最高初始杠杆倍数
    private BigDecimal notionalCap; // 该层对应的名义价值上限
    private BigDecimal notionalFloor; // 该层对应的名义价值下限
    private BigDecimal maintMarginRatio; // 该层对应的维持保证金率

    public int getBracket() {
        return bracket;
    }

    public void setBracket(int bracket) {
        this.bracket = bracket;
    }

    public int getInitialLeverage() {
        return initialLeverage;
    }

    public void setInitialLeverage(int initialLeverage) {
        this.initialLeverage = initialLeverage;
    }

    public BigDecimal getNotionalCap() {
        return notionalCap;
    }

    public void setNotionalCap(BigDecimal notionalCap) {
        this.notionalCap = notionalCap;
    }

    public BigDecimal getNotionalFloor() {
        return notionalFloor;
    }

    public void setNotionalFloor(BigDecimal notionalFloor) {
        this.notionalFloor = notionalFloor;
    }

    public BigDecimal getMaintMarginRatio() {
        return maintMarginRatio;
    }

    public void setMaintMarginRatio(BigDecimal maintMarginRatio) {
        this.maintMarginRatio = maintMarginRatio;
    }

    @Override
    public String toString() {
        return "{" +
                "bracket=" + bracket +
                ", initialLeverage=" + initialLeverage +
                ", notionalCap=" + notionalCap +
                ", notionalFloor=" + notionalFloor +
                ", maintMarginRatio=" + maintMarginRatio +
                '}';
    }
}
