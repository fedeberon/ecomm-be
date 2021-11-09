package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Cart;
import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;

public interface ICheckoutService {
    Checkout get(Long id);

    Checkout save(Cart cart);

    Checkout changeStateTo(CheckoutState state, Long checkoutId);
}
