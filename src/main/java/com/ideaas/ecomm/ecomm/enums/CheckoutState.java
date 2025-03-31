package com.ideaas.ecomm.ecomm.enums;

import lombok.Getter;

import java.util.Arrays;

public enum CheckoutState {

    ACTIVE("Activo"),
    INACTIVE("Inactivo"),
    PAID_OUT("Pagado"),
    IN_PROCESS("En Proceso"),
    REJECTED("Rechazado"),
    BUDGET("Presupuesto"),
    COMPLETED("Completado"),
    PENDING("Pendiente");

    private String value;

    public String getValue() {
        return value;
    }

    CheckoutState(final String value) {
        this.value = value;
    }

    public static CheckoutState findByStatus(final String abbr){
        return Arrays.stream(values())
                .filter(checkoutState -> checkoutState.value.equals(abbr)).findFirst().orElse(null);
    }



}
