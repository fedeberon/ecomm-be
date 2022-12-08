package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Brand;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.services.interfaces.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
@CrossOrigin
public class BrandController {


    private IBrandService brandService;

    @Autowired
    public BrandController(final IBrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    private ResponseEntity<List<Brand>> findAll() {
        List<Brand> brands = brandService.findAll();

        return ResponseEntity.ok(brands);
    }

    @GetMapping("{id}")
    private ResponseEntity<Brand> findByID(@PathVariable final Long id) {
        Brand brands = brandService.findById(id);

        return ResponseEntity.ok(brands);
    }

    @PostMapping
    private ResponseEntity<Brand> save(@RequestBody Brand brand) {
        final Brand savedBrand = brandService.save(brand);

        return ResponseEntity.ok(savedBrand);
    }

    @PutMapping
    private ResponseEntity<Brand> update(@RequestBody Brand brand) {
        final Brand updatedBrand = brandService.update(brand);

        return ResponseEntity.ok(updatedBrand);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable final Long id){
        final Brand brandToDelete = brandService.get(id);
        brandService.delete(brandToDelete);

        return ResponseEntity.accepted().body("Size deleted succesfully");
    }

    @GetMapping("{id}/products")
    private ResponseEntity<List<Product>> findProductsInBrand(@PathVariable final Long id) {
        List<Product> products = brandService.findProductsInBrand(id);

        return ResponseEntity.ok(products);
    }

}
