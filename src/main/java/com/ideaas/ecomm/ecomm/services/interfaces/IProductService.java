package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Product;

import java.util.List;

public interface IProductService {

    List<Product> findAll();

    Product save(Product product);
}
