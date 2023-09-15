package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Brand;
import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
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

    List<Product> searchByBrand(List<SearchRequest.BrandRequest> brands);

    void discountAmountStock(List<ProductToCart> productToCarts);

    void increaseAmountOfSales(List<ProductToCart> productToCarts);

    List<Product> searchByCategories(List<SearchRequest.CategoriesRequest> categories);

    List<Product> findAll();

    Product deleteProduct(long id);

    Product activateProduct(long id);

    Product update(Long id, Product product);

    void deleteImageOfProduct(final Product product, final String imageName);

    List<Product> relationship(Long id);

    public List<Product> searchProducts(String name, Collection<Category> categories, Collection<Brand> brands, String orderBy);
}