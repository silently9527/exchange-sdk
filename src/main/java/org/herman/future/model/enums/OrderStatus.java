package org.herman.future.model.enums;


import org.herman.utils.EnumLookup;

/**
 * SUBMITTED, PARTIALFILLED, CANCELLING. PARTIALCANCELED FILLED CANCELED CREATED
 */
public enum OrderStatus {
    NEW("new"),
    PARTIALLY_FILLED("partial-filled"),
    FILLED("filled"),
    CANCELED("canceled"),
    EXPIRED("expired"),
    EXPIRED_IN_MATCH("expired-in-match"),
    INVALID("invalid");


    private final String code;

    OrderStatus(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    private static final EnumLookup<OrderStatus> lookup = new EnumLookup<>(OrderStatus.class);

    public static OrderStatus lookup(String name) {
        return lookup.lookup(name);
    }
}
