package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Cart;
import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICheckoutService {
    Checkout get(Long id);

    Checkout save(Cart cart, CheckoutState state);

    Checkout changeStateTo(CheckoutState state, Long checkoutId);

    Page<Checkout> findAll(Pageable pageable);

}
