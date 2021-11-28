package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Bill;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.services.interfaces.IBillService;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("shopping")
public class ShoppingController {

    private ICheckoutService checkoutService;
    private IUserService userService;
    private IBillService billService;

    @Autowired
    public ShoppingController(final ICheckoutService checkoutService,
                              final IUserService userService,
                              final IBillService billService) {
        this.checkoutService = checkoutService;
        this.userService = userService;
        this.billService = billService;
    }

    @GetMapping("mine/{username}")
    public ResponseEntity<List<Bill>> myShopping(@PathVariable String username) {
        User user = userService.get(username);
        List<Bill> bills = billService.findAllByUser(user);

        return ResponseEntity.ok(bills);
    }

}
