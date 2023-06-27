package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.converts.ProductConvert;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.exception.NotFoundException;
import com.ideaas.ecomm.ecomm.payload.ProductPayload;
import com.ideaas.ecomm.ecomm.payload.SearchRequest;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    private final IProductService productService;

    @Autowired
    public ProductController(final IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<Product> find(@RequestParam(defaultValue = "1") final Integer page,
                              @RequestParam(defaultValue = "12") final Integer size,
                              @RequestParam(defaultValue = "name") final String sortBy) {
        return productService.findAll(page, size, sortBy);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAll(){
        List<Product> products = productService.findAll();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/relationship/{id}")
    public ResponseEntity<List<Product>> findAllRelationship(@PathVariable Long id){
        List<Product> products = productService.relationship(id);

        return ResponseEntity.ok(products);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable final long id) {
        Product product =productService.deleteProduct(id);

        return ResponseEntity.ok(product);
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

    @GetMapping("/list")
    public List<Product> list() {
        return productService.findAll();
    }

    @PostMapping
    public ResponseEntity<Product> save(@RequestBody Product product) {
        Product productSaved = productService.save(product);
        productService.addImagesOnProduct(product);
        return ResponseEntity.accepted().body(productSaved);
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> update(@PathVariable final Long id, @RequestBody final Product product) {
        Product productToUpdate = productService.update(id, product);

        return ResponseEntity.accepted().body(productToUpdate);
    }

    @PutMapping("delete/{id}")
    public ResponseEntity<Product> UpdateDeleteProduct(@PathVariable final long id) {
        Product productToUpdate = productService.deleteProduct(id);

        return ResponseEntity.ok(productToUpdate);
    }

    @PutMapping("activate/{id}")
    public ResponseEntity<Product> activateProduct(@PathVariable final long id) {
        Product productToUpdate = productService.activateProduct(id);

        return ResponseEntity.ok(productToUpdate);
    }

    @GetMapping("/byType/{category}")
    public ResponseEntity<List<Product>> byType(final @PathVariable Long category) {
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

    @DeleteMapping("/delete/{productId}/{image}")
    public ResponseEntity deleteImage(@PathVariable Long productId, @PathVariable String image) {
        Product productToUpdate = productService.get(productId);
        productService.deleteImageOfProduct(productToUpdate, image);

        return ResponseEntity.accepted().build();
    }

}
