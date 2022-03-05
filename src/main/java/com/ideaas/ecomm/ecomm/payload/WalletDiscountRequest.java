package com.ideaas.ecomm.ecomm.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletDiscountRequest {

    private Long checkoutId;

    private String username;
}
