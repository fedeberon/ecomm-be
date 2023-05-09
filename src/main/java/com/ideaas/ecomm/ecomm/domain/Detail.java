package com.ideaas.ecomm.ecomm.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Detail {

    @JsonProperty("id")
    private Integer productId;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("size")
    private String size;
}