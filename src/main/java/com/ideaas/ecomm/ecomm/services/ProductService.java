package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Brand;
import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.domain.Image;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
import com.ideaas.ecomm.ecomm.domain.Size;
import com.ideaas.ecomm.ecomm.payload.SearchRequest;
import com.ideaas.ecomm.ecomm.repository.ProductDao;
import com.ideaas.ecomm.ecomm.services.interfaces.ICategoryService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public Page<Product> findAll(int page, int size, String sortBy) {
        Sort sort = Sort.by(sortBy).descending();
        Page<Product> products = dao.findByDeleted(false, PageRequest.of(page, size, sort));
        
        products.forEach(product -> addImagesOnProduct(product));

        return products;
    }

    @Override
    public List<Product> All() {
        List<Product> products = (List<Product>) dao.findByDeleted(false);
        products.forEach(product -> addImagesOnProduct(product));
        
        return products;
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


    public void deleteImageOfProduct(final Product product, final String imageName) {
        fileService.deleteImage(product, imageName);
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
        List<Product> optionalProducts = dao.findAllByNameContainingIgnoreCaseAndDeleted(value,false);
        optionalProducts.forEach(oneProduct -> addImagesOnProduct(oneProduct));

        return optionalProducts;
    }

    @Override
    public List<Product> searchByBrand(List<SearchRequest.BrandRequest> brands) {
        Collection brandsList = convertToCollection(brands);
        List<Product> products = dao.searchAllByBrandInAndDeleted(brandsList, false);
        products.forEach(oneProduct -> addImagesOnProduct(oneProduct));
        
        return products;
    }

    @Override
    public List<Product> searchByCategories(List<SearchRequest.CategoriesRequest> categories) {
        Collection categoriesList = convertToCategoriesCollection(categories);
        List<Product> products =  dao.searchAllByCategoryInAndDeleted(categoriesList, false);
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
    public void increaseAmountOfSales(final List<ProductToCart> productToCarts) {
        productToCarts.forEach(productToCart -> {
            Product product = productToCart.getProduct();
            Long sales =  product.getSales() + productToCart.getQuantity();
            product.setSales(sales);
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

    @Override
    public Product activateProduct(long id) {
        Product product = this.get(id);
        product.setDeleted(false);
        this.save(product);

        return product;
    }

    @Override
    public Product update(Long id, Product product) {
        Product productToUpdate = this.get(id);
        String name = product.getName() != null ? product.getName() : productToUpdate.getName();
        String description = product.getDescription() != null ? product.getDescription() : productToUpdate.getDescription();
        String code = product.getCode() != null ? product.getCode() : productToUpdate.getCode();
        Double price = product.getPrice() != null ? product.getPrice() : productToUpdate.getPrice();
        Long stock = product.getStock() != null ? product.getStock() : productToUpdate.getStock();
        Category category = product.getCategory() != null ? product.getCategory() : productToUpdate.getCategory();
        Set<Size> sizes = product.getSizes() != null ? product.getSizes() : productToUpdate.getSizes();
        Brand brand = product.getBrand() != null ? product.getBrand() : productToUpdate.getBrand();
        Long points = product.getPoints() != null ? product.getPoints() : productToUpdate.getPoints();
        Boolean promo = product.getPromo() != null ? product.getPromo() : productToUpdate.getPromo();
        Boolean deleted = product.getDeleted() != null ? product.getDeleted() : productToUpdate.getDeleted();
        
        productToUpdate.setName(name);
        productToUpdate.setDescription(description);
        productToUpdate.setCode(code);
        productToUpdate.setPrice(price);
        productToUpdate.setStock(stock);
        productToUpdate.setCategory(category);
        productToUpdate.setSizes(sizes);
        productToUpdate.setBrand(brand);
        productToUpdate.setPoints(points);
        productToUpdate.setPromo(promo);
        productToUpdate.setDeleted(deleted);


        this.save(productToUpdate);
        return productToUpdate;
    }

}
