package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.Wallet;
import com.ideaas.ecomm.ecomm.enums.WalletTransactionType;
import com.ideaas.ecomm.ecomm.payload.WalletDiscountRequest;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import com.ideaas.ecomm.ecomm.services.interfaces.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wallet")
@CrossOrigin
public class WalletController {

    private ICheckoutService checkoutService;
    private IUserService userService;
    private IWalletService walletService;
    private IProductService productService;

    @Autowired
    public WalletController(final ICheckoutService checkoutService,
                            final IUserService userService,
                            final IWalletService walletService,
                            final IProductService productService) {
        this.checkoutService = checkoutService;
        this.userService = userService;
        this.walletService = walletService;
        this.productService = productService;
    }

    @PostMapping("/buyWithPoints")
    public ResponseEntity<String> buyWithPoints(final @RequestBody WalletDiscountRequest walletDiscountRequest) {
        Checkout checkout = checkoutService.get(walletDiscountRequest.getCheckoutId());
        User user = userService.get(walletDiscountRequest.getUsername());
        boolean validator = walletService.walletValidate(user, checkout.getProducts(), WalletTransactionType.SALE);
        if(validator){
            walletService.productToCartInWallet(user, checkout.getProducts(), WalletTransactionType.SALE);
            productService.discountAmountStock(checkout.getProducts());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(202).body("puntos insuficientes");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Wallet> addPoints(@RequestBody final Wallet wallet){
        final Wallet walletAdd = walletService.addPoints(wallet);

        return ResponseEntity.status(202).body(walletAdd);
    }

    @PostMapping("/remove")
    public ResponseEntity<Wallet> removePoints(@RequestBody final Wallet wallet){
        final Wallet walletRemove = walletService.removePoints(wallet);

        return ResponseEntity.status(202).body(walletRemove);
    }

}
