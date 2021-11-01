package com.ideaas.ecomm.ecomm.enums;

import lombok.Getter;

@SuppressWarnings("all")
@Getter
public enum IVAConditionType {

    NO_GRAVADO("1", 21.0),
    EXENTO("2", 21.0),
    O_POCIENTO("3", 21.0),
    DIEZ_COMA_CINDO("4", 10.5),
    VEINTIUNO_PORCIENTO("5", 21.0),
    VEINTISIETE_PORCIENTO("6", 27.0);

    private String code;

    private Double value;

    IVAConditionType(String code, Double value) {
        this.code = code;
        this.value = value;
    }
}
