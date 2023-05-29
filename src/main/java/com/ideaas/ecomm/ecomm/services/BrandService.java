package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Brand;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.repository.BrandDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IBrandService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BrandService implements IBrandService {

    private BrandDao dao;
    private IProductService productService;

    @Autowired
    public BrandService(final BrandDao dao, final IProductService productService) {
        this.dao = dao;
        this.productService = productService;
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

	@Override
	public Brand get(Long id) {
		return dao.findById(id).get();
	}

	@Override
	public void delete(Brand brandToDelete) {
        List<Product> products = productService.findAll();
        for (Product product: products){
            Long brandId = brandToDelete.getId();
            if(product.getBrand() != null && product.getBrand().getId() == brandId){
                product.setBrand(null);
            }
        }
		dao.delete(brandToDelete);
		
	}

    @Override
    public List<Product> findProductsInBrand(Long id) {
        Brand brand = this.findById(id);
        List<Product> products = productService.findAll();
        List<Product> productsOfBrand = new ArrayList<>();
        for (Product product: products){
            Long brandId = brand.getId();
            if(product.getBrand() != null && product.getBrand().getId() == brandId){
                productsOfBrand.add(product);
            }
        }

        return productsOfBrand;
    }

}
