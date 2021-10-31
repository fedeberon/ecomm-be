package com.ideaas.ecomm.ecomm.domain;

import lombok.Getter;

@Getter
public class Item {

    private Long id;
    private String code;
    private String description;
    private Long quantity;
    private Double price;

}
