package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.Store;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.exception.NotFoundException;
import com.ideaas.ecomm.ecomm.services.interfaces.IStoreService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import com.ideaas.ecomm.ecomm.utils.Onlyusernameobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/store")
@CrossOrigin
public class StoreController {

    private IStoreService storeService;
    private IUserService userService;

    @PostMapping
    private ResponseEntity<Store> save(@RequestBody Store store,
                                       @RequestParam(name = "ownerIds") Set<String> ownerIds) {
        Store savedStore = storeService.save(store, ownerIds);
        return ResponseEntity.accepted().body(savedStore);
    }

    @Autowired
    public StoreController(final IStoreService storeService, final IUserService userService) {
        this.storeService = storeService;
        this.userService = userService;
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

    @GetMapping("{id}/owners")
    private ResponseEntity<Set<String>> getOwnerIds(@PathVariable final Long id) {
        Store store = storeService.findById(id);
        Set<String> ownersId = new HashSet<>();
        for (User owner : store.getOwners()){
            ownersId.add(owner.getUsername());
        }
        return ResponseEntity.ok(ownersId);
    }

    @PutMapping
    private ResponseEntity<Store> update(@RequestBody Store store) {
        final Store updatedStore = storeService.update(store.getId(),store);
        return ResponseEntity.ok(updatedStore);
    }

    @GetMapping("/owners")
    private ResponseEntity<Store> updateOwners(@RequestParam(name = "storeId")  Long storeId, @RequestParam (name = "ownerIds")  Set<String> ownerIds) {
        final Store updatedStore = storeService.updateOwners(storeId, ownerIds);
        return ResponseEntity.ok(updatedStore);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable final Long id){
        final Store storeToDelete = storeService.get(id);
        storeService.delete(storeToDelete);

        return ResponseEntity.accepted().body("Store deleted succesfully");
    }

    @GetMapping("{id}/products")
    private ResponseEntity<List<Product>> findProductsInStore(@PathVariable final Long id) {
        List<Product> products = storeService.findProductsInStore(id);

        return ResponseEntity.ok(products);
    }

    @PostMapping("/{storeId}/addUser")
    public ResponseEntity<String> addUserToStore(
            @PathVariable Long storeId,
            @RequestBody Onlyusernameobject username
    ) {
        try {
            storeService.addUserToStore(storeId, username.getUsername());
            return ResponseEntity.ok("Usuario agregado a la tienda exitosamente.");
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{storeId}/{image}")
    public ResponseEntity<Store> deleteImage(@PathVariable Long storeId, @PathVariable String image) {
        System.out.println(storeId + "\n" + image);
        Store storeToUpdate = storeService.get(storeId);
        storeService.deleteLogoOfStore(storeToUpdate, image);

        return ResponseEntity.accepted().build();
    }
}
