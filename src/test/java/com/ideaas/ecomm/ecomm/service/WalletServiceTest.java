package com.ideaas.ecomm.ecomm.service;

import com.ideaas.ecomm.ecomm.domain.*;
import com.ideaas.ecomm.ecomm.enums.WalletTransactionType;
import com.ideaas.ecomm.ecomm.repository.WalletDao;
import com.ideaas.ecomm.ecomm.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class WalletServiceTest {

    private WalletDao walletDao;
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletDao = mock(WalletDao.class);
        walletService = new WalletService(walletDao);
    }

    @Test
    void testGetPointsWalletByUser_shouldSumAllWalletPoints() {
        User user = new User();
        List<Wallet> wallets = Arrays.asList(
                Wallet.builder().points(10L).build(),
                Wallet.builder().points(15L).build()
        );

        when(walletDao.findAllByUser(user)).thenReturn(wallets);

        Long result = walletService.getPointsWalletByUser(user);
        assertEquals(25L, result);
    }

    @Test
    void testGetActivePointsWalletByUser_shouldSumOnlyValidWalletPoints() {
        User user = new User();
        List<Wallet> wallets = Arrays.asList(
                Wallet.builder().points(10L).isConsumed(false).build(),
                Wallet.builder().points(20L).isConsumed(false).build()
        );
        when(walletDao.findByUserAndAndDateAfter(eq(user), any(LocalDateTime.class))).thenReturn(wallets);

        Long result = walletService.getActivePointsWalletByUser(user);

        assertEquals(30L, result);
    }

    @Test
    void testWalletValidate_shouldReturnTrueIfUserHasEnoughPoints() {
        User user = new User();
        Product product = Product.builder().price(100.0).promo(false).build();
        ProductToCart ptc = ProductToCart.builder().product(product).quantity(1).build();

        List<ProductToCart> productToCarts = Collections.singletonList(ptc);
        Wallet wallet = Wallet.builder().points(10L).isConsumed(false).build();

        when(walletDao.findByUserAndAndDateAfter(eq(user), any())).thenReturn(Collections.singletonList(wallet));

        boolean result = walletService.walletValidate(user, productToCarts, WalletTransactionType.BUY);
        assertTrue(result);
    }

    @Test
    void testProductToCartInWallet_shouldCreateWalletAndSave() {
        User user = new User();
        Product product = Product.builder().price(10.0).promo(false).build();
        ProductToCart ptc = ProductToCart.builder().product(product).quantity(2).build();
        List<ProductToCart> productToCarts = Collections.singletonList(ptc);

        // Devolver lista vacía cuando busque registros activos
        when(walletDao.findByUserAndAndDateAfter(eq(user), any())).thenReturn(Collections.emptyList());

        walletService.productToCartInWallet(user, productToCarts, WalletTransactionType.BUY);

        verify(walletDao, times(1)).saveAll(anyList());
    }

    @Test
    void testRemovePoints_shouldRegisterConsumedAndSaveNegativeWallet() {
        User user = new User();
        Wallet wallet = Wallet.builder().user(user).points(10L).build();
        Wallet register1 = Wallet.builder().points(5L).isConsumed(false).build();
        Wallet register2 = Wallet.builder().points(10L).isConsumed(false).build();

        when(walletDao.findByUserAndAndDateAfter(eq(user), any())).thenReturn(Arrays.asList(register1, register2));
        when(walletDao.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet result = walletService.removePoints(wallet);

        assertNotNull(result);
        assertEquals(-10L, result.getPoints());
        verify(walletDao, times(2)).save(any(Wallet.class));
    }
    @Test
    void testAddPoints_shouldSaveWallet() {
        Wallet wallet = Wallet.builder().points(100L).build();
        when(walletDao.save(wallet)).thenReturn(wallet);

        Wallet result = walletService.addPoints(wallet);

        assertNotNull(result);
        assertEquals(100L, result.getPoints());
        verify(walletDao, times(1)).save(wallet);
    }
    @Test
    void testGetOne_shouldReturnWalletById() {
        Wallet wallet = Wallet.builder().points(50L).build();
        when(walletDao.getById(1L)).thenReturn(wallet);

        Wallet result = walletService.getOne(1L);
        assertEquals(50L, result.getPoints());
    }
}
