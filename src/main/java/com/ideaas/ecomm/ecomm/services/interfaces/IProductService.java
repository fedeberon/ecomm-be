package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Product;

import java.util.List;

public interface IProductService {

    List<Product> findAll();

    Product save(Product product);

    Product get(Long id);

    void addImagesOnProduct(Product product);

    List<Product> byCategory(String category);

    List<Product> search(String value);
}
