package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.payload.CheckoutResponse;

public interface ICheckoutService {
    Checkout save(CheckoutResponse response);
}
