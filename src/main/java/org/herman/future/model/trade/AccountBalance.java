package org.herman.future.model.trade;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.herman.Constants;

import java.math.BigDecimal;

/**
 * [
 * {
 * "accountAlias": "SgsR",    // 账户唯一识别码
 * "asset": "USDT",        // 资产
 * "balance": "122607.35137903",   // 总余额
 * "crossWalletBalance": "23.72469206", // 全仓余额
 * "crossUnPnl": "0.00000000"  // 全仓持仓未实现盈亏
 * "availableBalance": "23.72469206",       // 下单可用余额
 * "maxWithdrawAmount": "23.72469206",     // 最大可转出余额
 * "marginAvailable": true,    // 是否可用作联合保证金
 * "updateTime": 1617939110373
 * }
 * ]
 */
public class AccountBalance {

    private String asset;
    private BigDecimal balance;
    private BigDecimal crossWalletBalance;
    private BigDecimal crossUnPnl;
    private BigDecimal availableBalance;
    private BigDecimal maxWithdrawAmount;
    private Boolean marginAvailable;

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCrossWalletBalance() {
        return crossWalletBalance;
    }

    public void setCrossWalletBalance(BigDecimal crossWalletBalance) {
        this.crossWalletBalance = crossWalletBalance;
    }

    public BigDecimal getCrossUnPnl() {
        return crossUnPnl;
    }

    public void setCrossUnPnl(BigDecimal crossUnPnl) {
        this.crossUnPnl = crossUnPnl;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getMaxWithdrawAmount() {
        return maxWithdrawAmount;
    }

    public void setMaxWithdrawAmount(BigDecimal maxWithdrawAmount) {
        this.maxWithdrawAmount = maxWithdrawAmount;
    }

    public Boolean getMarginAvailable() {
        return marginAvailable;
    }

    public void setMarginAvailable(Boolean marginAvailable) {
        this.marginAvailable = marginAvailable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, Constants.TO_STRING_BUILDER_STYLE)
                .append("asset", asset)
                .append("balance", balance)
                .append("crossWalletBalance", crossWalletBalance)
                .append("crossUnPnl", crossUnPnl)
                .append("availableBalance", availableBalance)
                .append("maxWithdrawAmount", maxWithdrawAmount)
                .append("marginAvailable", marginAvailable)
                .toString();
    }
}
