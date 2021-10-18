package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Image;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.exception.NotFoundException;
import com.ideaas.ecomm.ecomm.repository.ProductDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    private ProductDao dao;

    private FileService fileService;

    @Autowired
    public ProductService(final ProductDao dao, final FileService fileService) {
        this.dao = dao;
        this.fileService = fileService;
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
}
