package com.ideaas.ecomm.ecomm.service;
import com.ideaas.ecomm.ecomm.domain.Callback;
import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
import com.ideaas.ecomm.ecomm.repository.CallbackDao;
import com.ideaas.ecomm.ecomm.services.CallbackService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class CallbackServiceTest {
    @Mock
    private CallbackDao callbackDao;

    @Mock
    private IProductService productService;

    @InjectMocks
    private CallbackService callbackService;

    private Callback callback;

    @BeforeEach
    void setUp() {
        callback = new Callback();
        callback.setId(1L);

        Checkout checkout = new Checkout();
        ProductToCart productToCart = new ProductToCart();
        productToCart.setProduct(new Product());
        checkout.setProducts(Arrays.asList(productToCart));

        callback.setCheckout(checkout);
    }

    @Test
    void testSave() {
        when(callbackDao.save(callback)).thenReturn(callback);

        Callback result = callbackService.save(callback);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        verify(callbackDao, times(1)).save(callback);
    }

    @Test
    void testGet_WhenCallbackExists() {
        when(callbackDao.findById(1L)).thenReturn(Optional.of(callback));

        Callback result = callbackService.get(1L);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        verify(callbackDao, times(1)).findById(1L);
        verify(productService, times(1)).addImagesOnProduct(any());
    }

    @Test
    void testGet_WhenCallbackDoesNotExist() {
        when(callbackDao.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> callbackService.get(2L));
        verify(callbackDao, times(1)).findById(2L);
    }
}
