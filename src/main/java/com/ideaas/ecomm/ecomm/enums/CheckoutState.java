package com.ideaas.ecomm.ecomm.enums;

import java.util.Arrays;

public enum CheckoutState {

    ACTIVE("Activo"),
    INACTIVE("Inactivo"),
    PAID_OUT("Pagado"),
    IN_PROCESS("En Proceso"),
    REJECTED("Rechazado"),
    BUDGET("Presupuesto");


    private String value;

    CheckoutState(final String value) {
        this.value = value;
    }

    public static CheckoutState findByStatus(final String abbr){
        return Arrays.stream(values())
                .filter(checkoutState -> checkoutState.value.equals(abbr)).findFirst().orElse(null);
    }
}
