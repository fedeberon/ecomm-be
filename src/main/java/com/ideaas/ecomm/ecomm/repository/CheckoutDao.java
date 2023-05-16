package com.ideaas.ecomm.ecomm.repository;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckoutDao extends JpaRepository<Checkout, Long> {

    Checkout getCheckoutByCheckoutState(CheckoutState checkoutState);

    List<Checkout> findAllById(Long id);

}
