package com.ideaas.ecomm.ecomm.repository;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckoutDao extends JpaRepository<Checkout, Long> {

    Optional<Checkout> getCheckoutByState(CheckoutState state);

}
