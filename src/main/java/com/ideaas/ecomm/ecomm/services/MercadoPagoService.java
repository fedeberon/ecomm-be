package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import com.ideaas.ecomm.ecomm.services.interfaces.IMercadoPagoService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPConfException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.ExcludedPaymentMethod;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.PaymentMethods;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@Service
public class MercadoPagoService implements IMercadoPagoService {

    @Value("${mercadoPagoCallBack}")
    private String callbackurl;
    @Value("${mercadoPagoNotificationUrl}")
    private String notificationUrl;
    @Value("${mercadoPagoGetPayment}")
    private String getPayment;


    private IProductService productService;
    private ICheckoutService checkoutService;

    private final String ACCESS_TOKEN = "TEST-7210183612571177-101815-da1016bb668135d6ba273a4465407cca-1515196309";

    @Autowired
    public MercadoPagoService(IProductService productService, ICheckoutService checkoutService) {
        this.productService = productService;
        this.checkoutService = checkoutService;

        try {
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

        try {
            JSONObject json = new JSONObject(restTemplate.exchange(requestEntity, String.class).getBody());
            String status = json.getString("status");
            Long externalReference = Long.parseLong(json.getString("external_reference"));

            Checkout checkout = checkoutService.get(externalReference);

            if (status.equals("approved")) {
                checkoutService.changeStateTo(CheckoutState.PAID_OUT, externalReference);
            } else if (isInProcessStatus(status)) {
                checkoutService.changeStateTo(CheckoutState.IN_PROCESS, externalReference);
            } else {
                checkoutService.changeStateTo(CheckoutState.REJECTED, externalReference);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isInProcessStatus(String status) {
        return status.equals("pending") || status.equals("authorized")
                || status.equals("in_process") || status.equals("in_mediation");
    }


    @Override
    public Preference createPreference(final Checkout checkout) {
        try {
            Preference preference = preparePreference(checkout);
            ArrayList<Item> getItemsFromCheckout = getItemsFromCheckout(checkout);

            BackUrls backUrls = new BackUrls(
                    callbackurl,
                    callbackurl,
                    callbackurl);
            preference.setAutoReturn(Preference.AutoReturn.all);
            preference.setBackUrls(backUrls);
            preference.setExternalReference(String.valueOf(checkout.getId()));
            preference.setPaymentMethods(removeCash());
            preference.setNotificationUrl(notificationUrl);
            preference.setItems(getItemsFromCheckout);
            preference.save();

            return preference;
        } catch (MPException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Item> getItemsFromCheckout(Checkout checkout){
        ArrayList<Item> getItemsFromCheckout = new ArrayList<>();
        List<ProductToCart> products = checkout.getProducts();
        for (ProductToCart pro: products){
            Product product = pro.getProduct();
            productService.addImagesOnProduct(product);
            Item item = new Item();

            item.setCategoryId(product.getCategory().getId().toString());
            item.setDescription(product.getDescription());
            item.setId(product.getId().toString());
            item.setPictureUrl(!product.getImages().isEmpty() ? product.getImages().get(0).getLink() : "");
            item.setQuantity(pro.getQuantity());
            item.setTitle(product.getName());
            item.setUnitPrice(pro.getPrice().floatValue());

            getItemsFromCheckout.add(item);
        }
        return getItemsFromCheckout;
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
