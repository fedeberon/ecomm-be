package com.ideaas.ecomm.ecomm.enums;

import lombok.Getter;

@Getter
public enum IdCartType {

    DNI("96"),
    CUIT("80");

    private String code;

    IdCartType(final String code) {
        this.code = code;
    }
}
