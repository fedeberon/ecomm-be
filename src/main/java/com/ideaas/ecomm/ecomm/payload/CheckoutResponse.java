package com.ideaas.ecomm.ecomm.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CheckoutResponse {

    private Long productId;
    private Long quantity;

}
