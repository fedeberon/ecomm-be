package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Brand;
import com.ideaas.ecomm.ecomm.domain.Product;

import java.util.List;

public interface IBrandService {
    List<Brand> findAll();

    Brand findById(Long id);

    Brand save(Brand brand);

    Brand update(Brand brand);

    Brand get(Long id);

    void delete(Brand brandToDelete);

    List<Product> findProductsInBrand(Long id);
}
