package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.mercadopago.resources.Preference;

public interface IMercadoPagoService {
    Preference createPreference(Checkout checkout);
}
