package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.Stock;
import com.ideaas.ecomm.ecomm.services.ProductService;
import com.ideaas.ecomm.ecomm.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("stock")
public class StockController {

    private StockService stockService;
    private ProductService productService;

    @Autowired
    public StockController(final StockService stockService,
                           final ProductService productService) {
        this.stockService = stockService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Stock>> findAll() {
        List<Stock> stock = stockService.findAll();

        return ResponseEntity.ok(stock);
    }

    @GetMapping("/product/{id}")
    private ResponseEntity<Stock> findAllByProduct(final @PathVariable Long id) {
        final Product product = productService.get(id);
        Stock stock = stockService.getBy(product);

        return ResponseEntity.ok(stock);
    }

    @PostMapping("list")
    private ResponseEntity<Stock> saveAll(final @RequestBody Stock stock) {
        stockService.save(stock);

        return ResponseEntity.ok(stock);
    }


    @GetMapping("{id}")
    private ResponseEntity<Stock> findAllBy(final @PathVariable Long id) {
        Stock stock = stockService.getBy(id);

        return ResponseEntity.ok(stock);
    }
}
