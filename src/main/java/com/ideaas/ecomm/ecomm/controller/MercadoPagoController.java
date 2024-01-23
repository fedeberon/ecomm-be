package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.*;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import com.ideaas.ecomm.ecomm.services.interfaces.ICallbackService;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import com.ideaas.ecomm.ecomm.services.interfaces.IMercadoPagoService;
import com.mercadopago.resources.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> mercadoPagoCheckout(@RequestBody CheckoutRequest checkoutRequest) {
        String username = checkoutRequest.getUsername();
        List<Detail> details = checkoutRequest.getDetails();

        final Cart cart = new Cart.CartBuilder().withDetails(details).build();
        //Se indica que la nueva compra, obviamente, esta en proceso
        final Checkout checkout = checkoutService.save(cart, CheckoutState.IN_PROCESS, username);
        final Preference preference = mercadoPagoService.createPreference(checkout);

        return ResponseEntity.ok(preference.getInitPoint());
    }

    @PostMapping("callback")
    private ResponseEntity callback(@RequestBody Callback callback) {
        //Obtengo de nuevo el checkout de la compra en proceso.
        Checkout checkout = checkoutService.get(Long.parseLong(callback.getExternalReference()));
        //Obtiene el estatus del callback (approved, pending, null, rejected, cancelled)...
        final String state = callback.getStatus() != null ? callback.getStatus() : "REJECTED";
        //Determinan las acciones a realizar en base al estado.
        if (state.equals("approved")) {
            checkout.setCheckoutState(CheckoutState.PAID_OUT);
        }else if (state.equals("pending")){
            checkout.setCheckoutState(CheckoutState.IN_PROCESS);
        }else{
            checkout.setCheckoutState(CheckoutState.REJECTED);
        }

        callback.setCheckout(checkout);
        callbackService.save(callback);

        return generateAnswer(callback, checkout);
    }

    private static ResponseEntity generateAnswer(Callback callback, Checkout checkout){
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(new StringBuilder()
                        .append("{")
                        .append("\"checkoutId\": \"").append(callback.getCheckout().getId()).append("\",")
                        .append("\"checkoutState\": \"").append(checkout.getCheckoutState()).append("\",")
                        .append("\"message\": \"Success! Payment callback received.\"")
                        .append("}")
                        .toString());
    }
}
