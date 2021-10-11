package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.repository.ProductDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    private ProductDao dao;

    @Autowired
    public ProductService(ProductDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Product> findAll() {
        return dao.findAll();
    }

    @Override
    public Product save(final Product product) {
        return dao.save(product);
    }
}
