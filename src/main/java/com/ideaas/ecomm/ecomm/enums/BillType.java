package com.ideaas.ecomm.ecomm.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BillType {

    A("1"),
    B("6"),
    C("11");

    private String code;

    BillType(String code) {
        this.code = code;
    }

    public static BillType find(final String code) {
        return Arrays.stream(BillType.values())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported type %s.", code)));
    }
}
