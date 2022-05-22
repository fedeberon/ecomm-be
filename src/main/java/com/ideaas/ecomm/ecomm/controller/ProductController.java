package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.exception.NotFoundException;
import com.ideaas.ecomm.ecomm.payload.SearchRequest;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/product")
public class ProductController {

    private IProductService productService;

    @Autowired
    public ProductController(final IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> list(@RequestParam(defaultValue = "1") final Integer page, @RequestParam(defaultValue = "10") final Integer size) {
        List<Product> products = productService.findAll(page, size);
         return products;
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> get(@PathVariable final Long id) {
        try {
            Product product = productService.get(id);
            return ResponseEntity.ok().body(product);
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

    @GetMapping("/byType/{category}")
    public ResponseEntity<List<Product>> byType(final @PathVariable String category) {
        List<Product> products = productService.byCategory(category);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/{value}")
    public ResponseEntity<List<Product>> search(final @PathVariable String value) {
        List<Product> products = productService.search(value);

        return ResponseEntity.ok(products);
    }

    @PostMapping("/search/brands")
    public ResponseEntity<List<Product>> searchByBrand(final @RequestBody List<SearchRequest.BrandRequest> brandRequests) {
        List<Product> products = productService.searchByBrand(brandRequests);

        return ResponseEntity.ok(products);
    }


    @PostMapping("/search/categories")
    public ResponseEntity<List<Product>> searchByCategory(final @RequestBody List<SearchRequest.CategoriesRequest> categoryRequests) {
        List<Product> products = productService.searchByCategories(categoryRequests);

        return ResponseEntity.ok(products);
    }


    @PostMapping("/promotion")
    public ResponseEntity<Product> setPromotion(final @RequestBody Product product) {
        Product productToUpdate = productService.get(product.getId());
        productToUpdate.setPromo(product.getPromo());
        productService.save(productToUpdate);

        return ResponseEntity.ok(productToUpdate);
    }

}
