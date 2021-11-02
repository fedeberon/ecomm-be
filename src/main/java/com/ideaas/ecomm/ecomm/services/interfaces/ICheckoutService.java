package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import com.ideaas.ecomm.ecomm.payload.CheckoutResponse;

public interface ICheckoutService {
    Checkout get(Long id);

    Checkout save(CheckoutResponse response);

    Checkout changeStateTo(CheckoutState state, Long checkoutId);
}
