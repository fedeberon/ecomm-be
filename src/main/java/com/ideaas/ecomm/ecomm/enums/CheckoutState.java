package com.ideaas.ecomm.ecomm.enums;

import java.util.Arrays;

public enum CheckoutState {

    ACTIVE("approved"),
    INACTIVE("inactive"),
    PAID_OUT("Paid Out"),
    IN_PROCESS("in_process"),
    REJECTED("rejected");


    private String value;

    CheckoutState(final String value) {
        this.value = value;
    }

    public static CheckoutState findByStatus(final String abbr){
        return Arrays.stream(values())
                .filter(checkoutState -> checkoutState.value.equals(abbr)).findFirst().orElse(null);
    }
}
