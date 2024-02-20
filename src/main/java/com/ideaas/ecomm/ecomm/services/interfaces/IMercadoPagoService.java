package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.mercadopago.resources.Preference;
import org.springframework.stereotype.Service;

@Service
public interface IMercadoPagoService {
    Preference createPreference(Checkout checkout);

    void getMPPayment(final String paymentId);
}
