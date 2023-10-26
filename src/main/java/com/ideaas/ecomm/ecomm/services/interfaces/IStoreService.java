package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Store;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.User;

import java.util.List;
import java.util.Optional;

public interface IStoreService {
    List<Store> findAll();

    Store findById(Long id);

    Store save(Store Store);

    Store update(Store Store);

    Store get(Long id);

    void delete(Store storeToDelete);

    List<Product> findProductsInStore(Long id);

    Optional<User> getOwner(Store store);
}
