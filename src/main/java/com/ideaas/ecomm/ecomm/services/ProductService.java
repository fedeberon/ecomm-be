package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Brand;
import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.domain.Image;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.payload.SearchBrandRequest;
import com.ideaas.ecomm.ecomm.repository.ProductDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    private ProductDao dao;
    private FileService fileService;
    private CategoryService categoryService;

    @Autowired
    public ProductService(final ProductDao dao,
                          final FileService fileService,
                          final CategoryService categoryService) {
        this.dao = dao;
        this.fileService = fileService;
        this.categoryService = categoryService;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = dao.findAll();
        products.forEach(product -> addImagesOnProduct(product));
        return dao.findAll();
    }

    @Override
    public Product save(final Product product) {
        return dao.save(product);
    }


    @Override
    public Product get(final Long id) {
        Optional<Product> optionalProduct = dao.findById(id);
        addImagesOnProduct(optionalProduct.get());

        return optionalProduct.get();
    }


    @Override
    public void addImagesOnProduct(final Product product) {
        List<Image> images  = fileService.readFiles(product.getId().toString());
        images.forEach(image -> {
            String path = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/file/download/")
                    .path(product.getId().toString())
                    .path(File.separator)
                    .path(image.getUrl())
                    .toUriString();
            image.setLink(path);
        });
        product.setImages(images);
    }

    @Override
    public List<Product> byCategory(final String nameOfCategory) {
        Category category = categoryService.findAllByNameEquals(nameOfCategory);
        List<Product> optionalProducts = dao.findAllByCategory(category);

        return optionalProducts;
    }


    @Override
    public List<Product> search(final String value) {
        List<Product> optionalProducts = dao.findAllByNameIgnoreCase(value);
        optionalProducts.forEach(oneProduct -> addImagesOnProduct(oneProduct));

        return optionalProducts;
    }

    @Override
    public List<Product> searchByBrand(List<SearchBrandRequest.BrandRequest> brands) {
        Collection brandsList = convertToCollection(brands);
        return dao.searchAllByBrandIn(brandsList);
    }

    private Collection<Brand> convertToCollection(final List<SearchBrandRequest.BrandRequest> brandRequests) {
        if (brandRequests == null || brandRequests.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<Brand> collection = new ArrayList<>();
        for (SearchBrandRequest.BrandRequest v : brandRequests) {
            collection.add(new Brand(v.getId()));
        }
        return collection;
    }

}
