package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.exception.NotFoundException;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> list() {
        List<Product> products = productService.findAll();
         return products;
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> get(@PathVariable final Long id) {
        try {
            Product products = productService.get(id);
            return ResponseEntity.ok().body(products);
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> save(@RequestBody Product product) {
        Product productSaved = productService.save(product);

        return ResponseEntity.accepted().body(productSaved);
    }

    @PutMapping
    public ResponseEntity<Product> update(@RequestBody Product product) {
        Product productSaved = productService.save(product);

        return ResponseEntity.accepted().body(productSaved);
    }

}
