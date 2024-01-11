package com.ideaas.ecomm.ecomm.services;


import com.ideaas.ecomm.ecomm.domain.Image;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.Store;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.repository.StoreDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import com.ideaas.ecomm.ecomm.services.interfaces.IStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StoreService implements IStoreService {

    private StoreDao dao;
    private UserService userService;
    private FileService fileService;

    private IProductService productService;

    @Autowired
    public StoreService(final StoreDao dao, final UserService userService, final FileService fileService, final IProductService productService) {
        this.dao = dao;
        this.userService = userService;
        this.fileService = fileService;
        this.productService = productService;
    }

    @Override
    public List<Store> findAll() {
        List<Store> stores =  dao.findAllByDeletedFalse();
        for(Store store: stores){
            addLogoOnStore(store);
        }
        return stores;
    }

    @Override
    public Store findById(Long id) {
        Store store = dao.findById(id).orElse(null);
        addLogoOnStore(store);
        return store;
    }

    @Override
    public Store save(final Store store, final Set<String> ownerIds) {
        if (dao.existsByName(store.getName())) {
            throw new IllegalArgumentException("Store name already exists");
        }
        
        Set<User> owners  = new HashSet<>();
        for(String ownerId : ownerIds){
            Optional<User> owner = userService.get(ownerId);
            if (owner.isPresent()){
                owners.add(owner.get());
                store.setOwners(owners);
            }
        }
        return dao.save(store);
    }

    @Override
    @Transactional
    public Store update(Long id, Store store) {
        Store existingStore = dao.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Store Id: " + id));
        existingStore.setName(store.getName().isEmpty() ? existingStore.getName() : store.getName());
        existingStore.setDescription(store.getDescription().isEmpty() ? existingStore.getDescription() : store.getDescription());
        existingStore.setEmail(store.getEmail().isEmpty() ? existingStore.getEmail() : store.getEmail());
        existingStore.setAddress(store.getAddress().isEmpty() ? existingStore.getAddress() : store.getAddress());
        existingStore.setTelephone(store.getTelephone().isEmpty() ? existingStore.getTelephone() : store.getTelephone());
        existingStore.setSchedule(store.getSchedule().isEmpty() ? existingStore.getSchedule() : store.getSchedule());
        existingStore.setOwners(existingStore.getOwners());

        return dao.save(existingStore);
    }

	@Override
	public Store get(Long id) {
		Store store = dao.findById(id).get();
        addLogoOnStore(store);
        return store;
	}


    public List<Store> getStoresByUser(User user) {
        return dao.findByOwnersContaining(user);
    }

	@Override
	public void delete(Store storeToDelete) {
        storeToDelete.setDeleted(true);
        List<Product> productsToDelete = productService.findProductsInStore(storeToDelete);
        for (Product product: productsToDelete){
            product.setDeleted(true);
        }
		dao.save(storeToDelete);
	}

    @Override
    public List<Product> getProductsFromStore(Store store){
        List<Product> products = productService.findProductsInStore(store);
        return products;
    }

    @Override
    public void addLogoOnStore(final Store store) {
        List<Image> images = fileService.readFiles(store.getId().toString());
        if (images != null && !images.isEmpty()) {
            Image logo = images.get(0);
            if (logo != null) {
                String path = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/file/download/")
                        .path(store.getId().toString())
                        .path("/")
                        .path(logo.getUrl())
                        .toUriString();
                logo.setLink(path);
                store.setLogo(logo);
            } else {
                store.setLogo(null);
            }
        } else {
            store.setLogo(null);
        }
    }

    public void deleteLogoFromStore(final Store store, final String imageName) {
        fileService.deleteLogo(store, imageName);
    }

    @Override
    public void addUserToStore(Long storeId, String username) {
        // Buscar la tienda y el usuario por sus respectivos IDs
        Store store = findById(storeId);
        User user = userService.get(username).get();

        if (store != null && user != null) {
            store.getOwners().add(user);
            dao.save(store);
        }
    }

    @Override
    public Store updateOwners(Long storeId, final Set<String> ownerIds){
        Store store = findById(storeId);
        store.setOwners(new HashSet<>());
        for(String id: ownerIds){
            User user = userService.get(id).get();
            if (store != null && user != null) {
                store.getOwners().add(user);
            }
        }
        dao.save(store);
        return store;
    }


    @Override
    public void deleteLogoOfStore(final Store store, final String imageName){
        fileService.deleteLogo(store, imageName);
    }

    @Override
    public Set<Store> getStoresByUser(String username) {
        User user = userService.get(username).orElse(null);
        if (user != null) {
            //Obtiene todas aquellas tiendas que no tienen borrado logico
            Set<Store> userStores = user.getStores().stream()
                    .filter(store -> !Boolean.TRUE.equals(store.getDeleted()))
                    .collect(Collectors.toSet());
            for (Store store: userStores){
                addLogoOnStore(store);
            }
            return userStores;
        }
        return null;
    }
}
