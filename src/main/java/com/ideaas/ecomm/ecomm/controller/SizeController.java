package com.ideaas.ecomm.ecomm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideaas.ecomm.ecomm.domain.Talle;
import com.ideaas.ecomm.ecomm.services.interfaces.ISizeService;

@RestController
@RequestMapping("size")
public class SizeController {
    
    private ISizeService sizeService;

    @Autowired
    public SizeController(final ISizeService sizeService) {
        this.sizeService = sizeService;
    }

    @GetMapping 
    private ResponseEntity<List<Talle>> findAll() {
        List<Talle> talles = sizeService.findAll();
        
        return ResponseEntity.ok(talles);
    }
}
