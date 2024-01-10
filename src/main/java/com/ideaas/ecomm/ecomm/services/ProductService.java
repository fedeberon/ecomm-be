package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.*;
import com.ideaas.ecomm.ecomm.payload.SearchRequest;
import com.ideaas.ecomm.ecomm.repository.ProductDao;
import com.ideaas.ecomm.ecomm.repository.StoreDao;
import com.ideaas.ecomm.ecomm.services.interfaces.ICategoryService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

@Service
public class ProductService implements IProductService {

    private ProductDao dao;
    private StoreDao storeDao;
    private FileService fileService;
    private ICategoryService categoryService;

    private RecommendService recommendService;

    @Autowired
    public ProductService(final ProductDao dao,
                          final StoreDao storeDao,
                          final FileService fileService,
                          final ICategoryService categoryService,
                          final RecommendService recommendService) {
        this.dao = dao;
        this.storeDao = storeDao;
        this.fileService = fileService;
        this.categoryService = categoryService;
        this.recommendService = recommendService;
    }


    @Override
    public Page<Product> findAll(int page, int size, String sortBy) {
        Sort sort = Sort.by(sortBy).descending();
        Page<Product> products = dao.findByDeleted(false, PageRequest.of(page, size, sort));

        products.forEach(product -> setImagesAndLogo(product));

        return products;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products =  dao.findByDeleted(false);
        products.forEach(this::setImagesAndLogo);

        return products;
    }

    @Override
    public Product save(final Product product) {
        return dao.save(product);
    }


    @Override
    public Product get(final Long id) {
        Optional<Product> optionalProduct = dao.findById(id);
        setImagesAndLogo(optionalProduct.get());

        return optionalProduct.get();
    }

    private void setImagesAndLogo(final Product product) {
        addImagesOnProduct(product);
        Store store = product.getStore();
        if ( store != null) {
            Image logo = fileService.readFiles(store.getId().toString()).stream().findFirst().orElse(null);
            if (logo != null) {
                logo.setLink(getPath(logo, store.getId().toString()));
                store.setLogo(logo);
            }
            product.setStore(store);
        }

    }


    @Override
    public void addImagesOnProduct(final Product product) {
        List<Image> images  = fileService.readFiles(product.getId().toString());
        images.forEach(image -> {
            image.setLink(getPath(image, product.getId().toString()));
        });
        product.setImages(images);
    }

    private String getPath(Image image, String id){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file/download/")
                .path(id)
                .path("/")
                .path(image.getUrl())
                .toUriString();
    }

    public void deleteImageOfProduct(final Product product, final String imageName) {
        fileService.deleteImage(product, imageName);
    }

    @Override
    public List<Product> relationship(Long id) {
        final Product product = get(id);
        return dao.getRelationship(product.getName(), product.getCategory());
    }

    @Override
    public List<Product> byCategory(final Long id) {
        Category category = categoryService.findById(id);
        List<Product> optionalProducts = dao.findByCategoryAndDeleted(category,false);
        optionalProducts.forEach(this::setImagesAndLogo);
        return optionalProducts;
    }

    @Override
    public List<Product> search(final String value) {
        List<Product> optionalProducts = dao.findAllByNameContainingIgnoreCaseAndDeleted(value,false);
        optionalProducts.forEach(this::setImagesAndLogo);

        return optionalProducts;
    }

    @Override
    public List<Product> searchByBrand(List<SearchRequest.BrandRequest> brands) {
        Collection brandsList = convertToCollection(brands);
        List<Product> products = dao.searchAllByBrandInAndDeleted(brandsList, false);
        products.forEach(oneProduct -> setImagesAndLogo(oneProduct));

        return products;
    }

    @Override
    public List<Product> searchByCategories(List<SearchRequest.CategoriesRequest> categories) {
        Collection categoriesList = convertToCategoriesCollection(categories);
        List<Product> products =  dao.searchAllByCategoryInAndDeleted(categoriesList, false);
        products.forEach(oneProduct -> setImagesAndLogo(oneProduct));

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
    public Page<Product> searchProducts(String name,
                                        Collection<Category> categories,
                                        Collection<Brand> brands,
                                        String orderBy,
                                        Boolean asc,
                                        int page,
                                        int size){
        Pageable pageable = PageRequest.of(page , size, Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, orderBy));
        Page<Product> productPage;

        if (categories != null && !categories.isEmpty() && brands != null && !brands.isEmpty()) {
            productPage =  dao.findAllByNameContainingIgnoreCaseAndDeletedFalseAndCategoryInAndBrandIn(name, categories, brands, pageable);
        } else if (categories != null && !categories.isEmpty()) {
            productPage = dao.findAllByNameContainingIgnoreCaseAndDeletedFalseAndCategoryIn(name, categories, pageable);
        } else if (brands != null && !brands.isEmpty()) {
            productPage = dao.findAllByNameContainingIgnoreCaseAndDeletedFalseAndBrandIn(name, brands, pageable);
        } else {
            productPage = dao.findAllByNameContainingIgnoreCaseAndDeletedFalse(name, pageable);
        }

        productPage.forEach(oneProduct -> setImagesAndLogo(oneProduct));

        return productPage;
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
    public List<Product> getRecommendedProducts(Long id, Integer amount){
        List<Product> recommendedProducts = recommendService.generateRecommendedProducts(id, amount);
        recommendedProducts.stream().forEach(this::setImagesAndLogo);
        return recommendedProducts;
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

    //Usados para eliminar un comercio con todos sus productos...
    @Override
    public List<Product> byStore(Long storeId) {
        Store store = storeDao.getById(storeId);
        List<Product> products =  dao.findAllByStore(store);
        return products;
    }
}

