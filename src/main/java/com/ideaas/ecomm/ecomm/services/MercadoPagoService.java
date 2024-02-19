package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.services.interfaces.IMercadoPagoService;
import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPConfException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.ExcludedPaymentMethod;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.PaymentMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class MercadoPagoService implements IMercadoPagoService {

    @Value("${mercadoPagoCallBack}")
    private String callbackurl;
    @Value("${mercadoPagoNotificationUrl}")
    private String notificationUrl;

    @Value("${mercadoPagoGetPayment}")
    private String getPayment;

    private final String ACCESS_TOKEN = "TEST-7210183612571177-101815-da1016bb668135d6ba273a4465407cca-1515196309";

    public MercadoPagoService() {
        try {
            //Token de testing
            MercadoPago.SDK.setAccessToken(ACCESS_TOKEN);
        } catch (MPConfException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMPPayment(final String paymentId) {
        final String uri = getPayment + "/" + paymentId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ACCESS_TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(uri));

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.exchange(requestEntity, String.class).getBody();

        System.out.println(result);
    }


    @Override
    public Preference createPreference(final Checkout checkout) {
        try {
            Preference preference = preparePreference(checkout);

            BackUrls backUrls = new BackUrls(
                    callbackurl,
                    callbackurl,
                    callbackurl);
            preference.setAutoReturn(Preference.AutoReturn.all);
            preference.setBackUrls(backUrls);
            preference.setExternalReference(String.valueOf(checkout.getId()));
            preference.setPaymentMethods(removeCash());
            preference.setNotificationUrl(notificationUrl);
            preference.setAdditionalInfo("TEXT");
            preference.save();

            return preference;
        } catch (MPException e) {
            e.printStackTrace();
            return null;
        }
    }

    private PaymentMethods removeCash (){
        PaymentMethods paymentMethods = new PaymentMethods();
        ExcludedPaymentMethod excludedPaymentMethod = new ExcludedPaymentMethod();
        excludedPaymentMethod.setId("cash");
        paymentMethods.setExcludedPaymentMethods(new ArrayList<>(Collections.singletonList(excludedPaymentMethod)));

        return paymentMethods;
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
