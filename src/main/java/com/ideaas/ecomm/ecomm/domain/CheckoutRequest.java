package com.ideaas.ecomm.ecomm.domain;

import lombok.Data;

import java.util.List;

@Data
public class CheckoutRequest {
    private String username;
    private List<Detail> details;
}
