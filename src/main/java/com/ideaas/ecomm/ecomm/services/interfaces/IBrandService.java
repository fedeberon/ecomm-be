package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Brand;

import java.util.List;

public interface IBrandService {
    List<Brand> findAll();

    Brand findById(Long id);

    Brand save(Brand brand);

    Brand update(Brand brand);
}
