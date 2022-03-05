package com.ideaas.ecomm.ecomm.enums;

public enum WalletTransactionType {

    SALE(-1),
    BUY(1);

    private int value;

    WalletTransactionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static WalletTransactionType getByValue(int value) {
        for (WalletTransactionType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }
}

