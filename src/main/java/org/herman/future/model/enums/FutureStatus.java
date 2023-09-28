package org.herman.future.model.enums;

public enum FutureStatus {
    TRADING("交易中"),
    PAUSE("暂停中"),
    CLOSE("已下架"),
    UN_KNOW("已下架");

    private String label;

    FutureStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
