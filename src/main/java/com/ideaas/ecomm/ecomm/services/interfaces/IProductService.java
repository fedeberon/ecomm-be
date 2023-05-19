package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
import com.ideaas.ecomm.ecomm.payload.SearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    
    Page<Product> findAll(int page, int size, String sortBy);

    Product save(Product product);

    Product get(Long id);

    void addImagesOnProduct(Product product);

    List<Product> byCategory(String category);

    List<Product> search(String value);

    List<Product> searchByBrand(List<SearchRequest.BrandRequest> brands);

    void discountAmountStock(List<ProductToCart> productToCarts);

    void increaseAmountOfSales(List<ProductToCart> productToCarts);

    List<Product> searchByCategories(List<SearchRequest.CategoriesRequest> categories);

    List<Product> All();

    Product deleteProduct(long id);

    Product activateProduct(long id);

    Product update(Long id, Product product);

    void deleteImageOfProduct(final Product product, final String imageName);
}