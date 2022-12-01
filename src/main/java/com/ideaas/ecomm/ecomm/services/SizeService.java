package com.ideaas.ecomm.ecomm.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.Size;
import com.ideaas.ecomm.ecomm.repository.SizeDao;
import com.ideaas.ecomm.ecomm.services.interfaces.ISizeService;

@Service
public class SizeService implements ISizeService {

    private SizeDao dao;

    @Autowired
    public SizeService(SizeDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Size> findAll() {
        return dao.findAll();
    }

    @Override
    public Size save(Size size) {
        return dao.save(size);
    }

    @Override
    public Size get(Long id) {
        return dao.findById(id).get();
    }

    @Override
    public void delete(Size sizeToDelete) {
        Set<Product> products = sizeToDelete.getProducts();
        for (Product product: products){
            Set<Size> sizes = product.getSizes();
            sizes.removeIf(size -> size.id == sizeToDelete.getId());
            product.setSizes(sizes);
        }
        dao.delete(sizeToDelete);
    }
    
}
