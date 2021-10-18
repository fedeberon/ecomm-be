package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.payload.CheckoutResponse;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("checkout")
public class CheckoutController {

    private ICheckoutService checkoutService;

    @Autowired
    public CheckoutController(final ICheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public Checkout save(@RequestBody final CheckoutResponse response) {
        final Checkout checkout = checkoutService.save(response);

        return checkout;
    }


    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Checkout> get(@PathVariable final Long id) {
        final Checkout checkout = checkoutService.get(id);

        return ResponseEntity.ok(checkout);
    }

}
