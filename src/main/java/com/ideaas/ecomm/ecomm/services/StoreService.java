package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Store;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.repository.StoreDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IStoreService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreService implements IStoreService {

    private StoreDao dao;
    private IProductService productService;
    private IUserService userService;

    @Autowired
    public StoreService(final StoreDao dao, final IProductService productService) {
        this.dao = dao;
        this.productService = productService;
    }

    @Override
    public List<Store> findAll() {
        return dao.findAll();
    }

    @Override
    public Store findById(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    public Store save(final Store Store) {
        return dao.save(Store);
    }

    @Override
    @Transactional
    public Store update(Long id, Store updatedStore) {
        Store existingStore = dao.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Store Id: " + id));
        existingStore.setName(updatedStore.getName());
        existingStore.setOwner(updatedStore.getOwner());

        return dao.save(existingStore);
    }

	@Override
	public Store get(Long id) {
		return dao.findById(id).get();
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
}
