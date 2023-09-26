package org.herman.future.model.market;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.herman.Constants;

import java.math.BigDecimal;

public class SymbolPrice {

    private String symbol;

    private BigDecimal price;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, Constants.TO_STRING_BUILDER_STYLE).append("symbol", symbol)
                .append("price", price).toString();
    }
}
