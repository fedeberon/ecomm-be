package com.ideaas.ecomm.ecomm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping({"/", "/home"})
    public String home() {
        return "Sistema de Gesti&oacoute;n de eComm. IDEAAS 2022";
    }

}
