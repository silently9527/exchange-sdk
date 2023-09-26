package org.herman.future.model.user;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.herman.Constants;

import java.util.List;

public class AccountUpdate {

    private List<BalanceUpdate> balances;

    private List<PositionUpdate> positions;

    public List<BalanceUpdate> getBalances() {
        return balances;
    }

    public void setBalances(List<BalanceUpdate> balances) {
        this.balances = balances;
    }

    public List<PositionUpdate> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionUpdate> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, Constants.TO_STRING_BUILDER_STYLE).append("balances", balances)
                .append("positions", positions).toString();
    }
}
