package com.ideaas.ecomm.ecomm.enums;

import lombok.Getter;

@Getter
public enum BillType {

    A("1"),
    B("6");

    private String description;

    BillType(String description) {
        this.description = description;
    }
}
