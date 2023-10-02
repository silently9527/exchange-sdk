package org.herman.future.model.user;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.herman.Constants;

import java.util.List;

public class AccountUpdate {

    private List<BalanceUpdate> balances;

    private List<PositionUpdateEvent> positions;

    public List<BalanceUpdate> getBalances() {
        return balances;
    }

    public void setBalances(List<BalanceUpdate> balances) {
        this.balances = balances;
    }

    public List<PositionUpdateEvent> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionUpdateEvent> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, Constants.TO_STRING_BUILDER_STYLE).append("balances", balances)
                .append("positions", positions).toString();
    }
}
