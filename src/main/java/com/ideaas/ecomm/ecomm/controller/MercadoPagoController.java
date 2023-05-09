package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Callback;
import com.ideaas.ecomm.ecomm.domain.Cart;
import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.Detail;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import com.ideaas.ecomm.ecomm.services.interfaces.ICallbackService;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import com.ideaas.ecomm.ecomm.services.interfaces.IMercadoPagoService;
import com.mercadopago.resources.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("payment")
public class MercadoPagoController {

    private IMercadoPagoService mercadoPagoService;
    private ICheckoutService checkoutService;
    private ICallbackService callbackService;

    @Autowired
    public MercadoPagoController(final IMercadoPagoService mercadoPagoService,
                                 final ICheckoutService checkoutService,
                                 final ICallbackService callbackService) {
        this.mercadoPagoService = mercadoPagoService;
        this.checkoutService = checkoutService;
        this.callbackService = callbackService;
    }

    @PostMapping("checkout")
    public ResponseEntity<String> mercadoPagoCheckout(@RequestBody List<Detail> details) {
        final Cart cart = new Cart.CartBuilder().withDetails(details).build();
        final Checkout checkout = checkoutService.save(cart, CheckoutState.PAID_OUT);
        final Preference preference = mercadoPagoService.createPreference(checkout);

        return ResponseEntity.ok(preference.getInitPoint());
    }

    @PostMapping("callback")
    private ResponseEntity callback(@RequestBody Callback callback) {
        final Checkout checkout = checkoutService.get(Long.parseLong(callback.getExternalReference()));
        final CheckoutState state = CheckoutState.findByStatus(callback.getStatus());
        checkout.setCheckoutState(state);
        callback.setCheckout(checkout);
        callbackService.save(callback);

        final String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/callback/")
                .path(String.valueOf(callback.getId()))
                .toUriString();

        return ResponseEntity.
                status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }
}
