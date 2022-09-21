package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Brand;
import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.domain.Image;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
import com.ideaas.ecomm.ecomm.payload.SearchRequest;
import com.ideaas.ecomm.ecomm.repository.ProductDao;
import com.ideaas.ecomm.ecomm.services.interfaces.ICategoryService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private ICategoryService categoryService;

    @Autowired
    public ProductService(final ProductDao dao,
                          final FileService fileService,
                          final ICategoryService categoryService) {
        this.dao = dao;
        this.fileService = fileService;
        this.categoryService = categoryService;
    }

    @Override
    public Page<Product> findAll(int page, int size) {
        Page<Product> products = dao.findAll(PageRequest.of(page, size));
        products.forEach(product -> addImagesOnProduct(product));

        return products;
    }

    @Override
    public List<Product> All() {
        return (List<Product>) dao.findAll();
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
        optionalProducts.forEach(oneProduct -> addImagesOnProduct(oneProduct));
        return optionalProducts;
    }


    @Override
    public List<Product> search(final String value) {
        List<Product> optionalProducts = dao.findAllByNameContainingIgnoreCase(value);
        optionalProducts.forEach(oneProduct -> addImagesOnProduct(oneProduct));

        return optionalProducts;
    }

    @Override
    public List<Product> searchByBrand(List<SearchRequest.BrandRequest> brands) {
        Collection brandsList = convertToCollection(brands);
        List<Product> products = dao.searchAllByBrandIn(brandsList);
        products.forEach(oneProduct -> addImagesOnProduct(oneProduct));
        
        return products;
    }

    @Override
    public List<Product> searchByCategories(List<SearchRequest.CategoriesRequest> categories) {
        Collection categoriesList = convertToCategoriesCollection(categories);
        List<Product> products =  dao.searchAllByCategoryIn(categoriesList);
        products.forEach(oneProduct -> addImagesOnProduct(oneProduct));
        
        return products;
    }

    private Collection<Brand> convertToCollection(final List<SearchRequest.BrandRequest> brandRequests) {
        if (brandRequests == null || brandRequests.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<Brand> collection = new ArrayList<>();
        for (SearchRequest.BrandRequest v : brandRequests) {
            collection.add(new Brand(v.getId()));
        }
        return collection;
    }



    private Collection<Category> convertToCategoriesCollection(final List<SearchRequest.CategoriesRequest> categoryRequests) {
        if (categoryRequests == null || categoryRequests.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<Category> collection = new ArrayList<>();
        for (SearchRequest.CategoriesRequest v : categoryRequests) {
            collection.add(new Category(v.getId()));
        }
        return collection;
    }


    @Override
    public void discountAmountStock(final List<ProductToCart> productToCarts) {
        productToCarts.forEach(productToCart -> {
            Product product = productToCart.getProduct();
            Long stock =  product.getStock() - productToCart.getQuantity();
            product.setStock(stock);
            save(product);
        });
    }

    @Override
    public Product deleteProduct(long id) {
        Product product = this.get(id);
        product.setDeleted(true);
        this.save(product);
        
        return product;
    }

}
