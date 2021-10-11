package com.ideaas.ecomm.ecomm.enums;

public enum CheckoutState {

    ACTIVE("Active"),
    INACTIVE("Inactive"),
    PAID_OUT("Paid Out");

    private String value;

    CheckoutState(final String value) {
        this.value = value;
    }
}
