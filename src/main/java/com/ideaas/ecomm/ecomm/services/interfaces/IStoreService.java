package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Store;
import com.ideaas.ecomm.ecomm.domain.Product;

import java.util.List;
import java.util.Set;

public interface IStoreService {
    List<Store> findAll();

    Store findById(Long id);

    Store save(final Store store, final Set<String> ownerIds);

    Store update(Long id, Store updatedStore);
    Store updateOwners(Long storeId, final Set<String> ownerIds);

    Store get(Long id);

    void delete(Store storeToDelete);

    List<Product> findProductsInStore(Long id);

    void addLogoOnStore(Store store);

    void addUserToStore(Long storeId, String username);


    void deleteLogoOfStore(final Store store, final String imageName);

    Set<Store> getStoresByUser(String username);
}
