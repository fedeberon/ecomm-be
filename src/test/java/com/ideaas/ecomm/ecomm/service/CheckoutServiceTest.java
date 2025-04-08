package com.ideaas.ecomm.ecomm.service;

import com.ideaas.ecomm.ecomm.domain.*;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import com.ideaas.ecomm.ecomm.repository.CheckoutDao;
import com.ideaas.ecomm.ecomm.services.CheckoutService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import com.ideaas.ecomm.ecomm.services.interfaces.ISizeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {
    @Mock
    private CheckoutDao checkoutDao;

    @Mock
    private IProductService productService;

    @Mock
    private ISizeService sizeService;

    @InjectMocks
    private CheckoutService checkoutService;

    private Checkout checkout;

    @BeforeEach
    void setUp() {
        checkout = new Checkout();
        checkout.setId(1L);
        checkout.setCheckoutState(CheckoutState.PENDING);
    }

    @Test
    void testGet_WhenCheckoutExists() {
        when(checkoutDao.findById(1L)).thenReturn(Optional.of(checkout));
        Checkout result = checkoutService.get(1L);
        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        verify(checkoutDao, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        Cart cart = new Cart();
        cart.setDetails(new ArrayList<>());
        when(checkoutDao.save(any(Checkout.class))).thenReturn(checkout);

        Checkout result = checkoutService.save(cart, CheckoutState.COMPLETED);

        assertNotNull(result);
        assertEquals(CheckoutState.COMPLETED, result.getCheckoutState());
        verify(checkoutDao, times(1)).save(any(Checkout.class));
    }

    @Test
    void testFindAll() {
        Page<Checkout> pageMock = mock(Page.class);
        when(checkoutDao.findAll(any(Pageable.class))).thenReturn(pageMock);
        Page<Checkout> result = checkoutService.findAll(mock(Pageable.class));
        assertNotNull(result);
        verify(checkoutDao, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testSearch() {
        when(checkoutDao.findAllById(1L)).thenReturn(Arrays.asList(checkout));
        List<Checkout> result = checkoutService.search(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Long.valueOf(1L), result.get(0).getId());
        verify(checkoutDao, times(1)).findAllById(1L);
    }

    @Test
    void testChangeStateTo() {
        when(checkoutDao.findById(1L)).thenReturn(Optional.of(checkout));
        when(checkoutDao.save(any(Checkout.class))).thenReturn(checkout);

        Checkout result = checkoutService.changeStateTo(CheckoutState.COMPLETED, 1L);

        assertNotNull(result);
        assertEquals(CheckoutState.COMPLETED, result.getCheckoutState());
        verify(checkoutDao, times(1)).findById(1L);
        verify(checkoutDao, times(1)).save(any(Checkout.class));
    }
}
