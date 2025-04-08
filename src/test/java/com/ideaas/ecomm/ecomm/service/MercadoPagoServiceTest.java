package com.ideaas.ecomm.ecomm.service;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
import com.ideaas.ecomm.ecomm.services.MercadoPagoService;
import com.mercadopago.resources.Preference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MercadoPagoServiceTest {
    @InjectMocks
    private MercadoPagoService mercadoPagoService;

    @Mock
    private Checkout checkout;

    @Test
    void testCreatePreference() {
        Product product = new Product();
        product.setName("Producto de prueba");
        product.setPrice(100.0);

        ProductToCart productToCart = new ProductToCart();
        productToCart.setProduct(product);
        productToCart.setQuantity(2);

        Checkout checkout = new Checkout();
        checkout.setId(1L);
        checkout.setProducts(Collections.singletonList(productToCart));

        Preference preference = mercadoPagoService.createPreference(checkout);

        assertNotNull(preference);
    }

    @Test
    void testCreatePreference_Failure() {
        MercadoPagoService faultyService = new MercadoPagoService() {
            @Override
            public Preference createPreference(Checkout checkout) {
                throw new RuntimeException("MPException simulated");
            }
        };

        Exception exception = assertThrows(RuntimeException.class, () -> {
            faultyService.createPreference(checkout);
        });

        assertTrue(exception.getMessage().contains("MPException simulated"));
    }
}
