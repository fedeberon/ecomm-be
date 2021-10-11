package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private IUserService userService;

    @Autowired
    public AuthenticationController(IUserService userService) {
        this.userService = userService;
    }
}
