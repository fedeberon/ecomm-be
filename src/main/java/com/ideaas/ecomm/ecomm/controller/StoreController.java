package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Store;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.services.interfaces.IStoreService;
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
@RequestMapping("/store")
@CrossOrigin
public class StoreController {

    private IStoreService storeService;

    @Autowired
    public StoreController(final IStoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    private ResponseEntity<List<Store>> findAll() {
        List<Store> stores = storeService.findAll();

        return ResponseEntity.ok(stores);
    }

    @GetMapping("{id}")
    private ResponseEntity<Store> findByID(@PathVariable final Long id) {
        Store stores = storeService.findById(id);

        return ResponseEntity.ok(stores);
    }

    @PostMapping
    private ResponseEntity<Store> save(@RequestBody Store store) {
        Store savedStore = storeService.save(store);

        return ResponseEntity.accepted().body(savedStore);
    }

    @PutMapping
    private ResponseEntity<Store> update(@RequestBody Store store) {
        final Store updatedStore = storeService.update(store);

        return ResponseEntity.ok(updatedStore);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable final Long id){
        final Store storeToDelete = storeService.get(id);
        storeService.delete(storeToDelete);

        return ResponseEntity.accepted().body("Size deleted succesfully");
    }

    @GetMapping("{id}/products")
    private ResponseEntity<List<Product>> findProductsInStore(@PathVariable final Long id) {
        List<Product> products = storeService.findProductsInStore(id);

        return ResponseEntity.ok(products);
    }

    @GetMapping("{id}/owner")
    private ResponseEntity<User> getowner(@RequestBody Store store) {
        User owner = store.getOwner();

        return ResponseEntity.ok(owner);
    }

}
