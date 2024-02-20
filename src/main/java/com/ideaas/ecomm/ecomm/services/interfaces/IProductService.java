package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.*;
import com.ideaas.ecomm.ecomm.payload.SearchRequest;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface IProductService {

    Page<Product> findAll(int page, int size, String sortBy);

    Product save(Product product);

    Product get(Long id);

    void addImagesOnProduct(Product product);

    List<Product> byCategory(Long id);

    List<Product> search(String value);

    List<Product> getRecommendedProducts(Long id, Integer amount);

    List<Product> searchByBrand(List<SearchRequest.BrandRequest> brands);

    void discountAmountStock(List<ProductToCart> productToCarts);

    void increaseAmountOfSales(List<ProductToCart> productToCarts);

    List<Product> searchByCategories(List<SearchRequest.CategoriesRequest> categories);

    List<Product> findAll();

    Product deleteProduct(long id);


    Product activateProduct(long id);

    Product update(Long id, Product product);

    void deleteImageOfProduct(final Product product, final String imageName);

    void setImagesAndLogo(final Product product);

    List<Product> relationship(Long id);

    Page<Product> searchProducts(String name, Collection<Category> categories, Collection<Brand> brands, String orderBy, Boolean asc, int page, int size);

    List<Product> findProductsInStore(Store store); //Obtiene todos los productos asociados a un Store
}