package com.ideaas.ecomm.ecomm.services;


import com.ideaas.ecomm.ecomm.domain.Image;
import com.ideaas.ecomm.ecomm.domain.Store;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.repository.ScheduleDao;
import com.ideaas.ecomm.ecomm.repository.StoreDao;
import com.ideaas.ecomm.ecomm.repository.UserDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IStoreService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.util.*;

@Service
public class StoreService implements IStoreService {

    private StoreDao dao;
    private UserDao usrDao;
    private ScheduleDao scheduleDao;
    private IProductService productService;
    private UserService userService;
    private FileService fileService;

    @Autowired
    public StoreService(final StoreDao dao, final ScheduleDao scheduleDao, final UserService userService, final IProductService productService, final FileService fileService) {

        this.dao = dao;
        this.scheduleDao = scheduleDao;
        this.userService = userService;
        this.productService = productService;
        this.fileService = fileService;
    }

    @Override
    public List<Store> findAll() {
        List<Store> stores =  dao.findAll();
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
    public Store save(final Store store, final String creatorId) {
        Optional<User> creator = userService.get(creatorId);
        Set<User> owners = new HashSet<User>();
        owners.add(userService.get(creatorId).get());
        store.setOwners(owners);
        return dao.save(store);
    }

    @Override
    @Transactional
    public Store update(Long id, Store updatedStore) {
        Store existingStore = dao.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Store Id: " + id));
        existingStore.setName(updatedStore.getName());
        existingStore.setOwners(updatedStore.getOwners());

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
        List<Product> products = productService.findAll();
        for (Product product: products){
            Long storeId = storeToDelete.getId();
            if(product.getStore() != null && product.getStore().getId() == storeId){
                product.setStore(null);
            }
        }
		dao.delete(storeToDelete);
		
	}

    @Override
    public List<Product> findProductsInStore(Long id) {
        Store Store = this.findById(id);
        List<Product> products = productService.findAll();
        List<Product> productsOfStore = new ArrayList<>();
        for (Product product: products){
            Long StoreId = Store.getId();
            if(product.getStore() != null && product.getStore().getId() == StoreId){
                productsOfStore.add(product);
            }
        }

        return productsOfStore;
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
                        .path(File.separator)
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
}
