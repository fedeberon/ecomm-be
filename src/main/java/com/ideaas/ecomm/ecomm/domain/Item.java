package com.ideaas.ecomm.ecomm.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Item {

    private Long id;
    private String code;
    private String description;
    private Integer quantity;
    private Double price;

}
