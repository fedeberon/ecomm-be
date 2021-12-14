package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Brand;
import com.ideaas.ecomm.ecomm.repository.BrandDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService implements IBrandService {

    private BrandDao dao;

    @Autowired
    public BrandService(final BrandDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Brand> findAll() {
        return dao.findAll();
    }

    @Override
    public Brand findById(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    public Brand save(Brand brand) {
        return dao.save(brand);
    }

    @Override
    public Brand update(Brand brand) {
        return dao.save(brand);
    }

}
