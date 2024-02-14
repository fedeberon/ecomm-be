package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.services.interfaces.IMercadoPagoService;
import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPConfException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MercadoPagoService implements IMercadoPagoService {

    @Value("${mercadoPagoCallBack}")
    private String callbackurl;


    public MercadoPagoService() {
        try {
            //Token de testing
            MercadoPago.SDK.setAccessToken("TEST-7210183612571177-101815-da1016bb668135d6ba273a4465407cca-1515196309");
        } catch (MPConfException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Preference createPreference(final Checkout checkout) {
        try {
            Preference preference = preparePreference(checkout);

            BackUrls backUrls = new BackUrls(
                    callbackurl,
                    callbackurl,
                    callbackurl);
            preference.setBackUrls(backUrls);
            preference.setExternalReference(String.valueOf(checkout.getId()));

            preference.save();

            return preference;
        } catch (MPException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Preference preparePreference(final Checkout checkout) {
        Preference preference = new Preference();
        checkout.getProducts().forEach((product -> {
            Item item = new Item();
            item.setTitle(product.getProduct().getName())
                    .setQuantity(product.getQuantity())
                    .setUnitPrice(product.getProduct().getPrice().floatValue());
            preference.appendItem(item);
        }));

        return preference;
    }

}
