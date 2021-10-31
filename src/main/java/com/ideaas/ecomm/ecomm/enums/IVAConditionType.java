package com.ideaas.ecomm.ecomm.enums;

import lombok.Getter;

@SuppressWarnings("all")
@Getter
public enum IVAConditionType {

    NO_GRAVADO("1"),
    EXENTO("2"),
    O_POCIENTO("3"),
    DIEZ_COMA_CINDO("4"),
    VEINTIUNO_PORCIENTO("5"),
    VEINTISIETE_PORCIENTO("6");

    private String code;

    IVAConditionType(String code) {
        this.code = code;
    }
}
