package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
import com.ideaas.ecomm.ecomm.payload.SearchBrandRequest;

import java.util.List;

public interface IProductService {

    List<Product> findAll();

    Product save(Product product);

    Product get(Long id);

    void addImagesOnProduct(Product product);

    List<Product> byCategory(String category);

    List<Product> search(String value);

    List<Product> searchByBrand(List<SearchBrandRequest.BrandRequest> brands);

    void discountAmountStock(List<ProductToCart> productToCarts);

    List<Product> searchByCategories(List<SearchBrandRequest.CategoriesRequest> categories);
}
