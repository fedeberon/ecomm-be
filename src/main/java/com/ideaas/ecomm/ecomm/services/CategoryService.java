package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.repository.CategoryDao;
import com.ideaas.ecomm.ecomm.services.interfaces.ICategoryService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    private CategoryDao dao;
    private IProductService productService;
    @Lazy
    @Autowired
    public CategoryService(CategoryDao dao, final IProductService productService) {
        this.dao = dao;
        this.productService = productService;
    }

    @Override
    public List<Category> findAll() {
        return dao.findAll();
    }

    @Override
    public Category findAllByNameEquals(final String nameCategory) {
        Optional<Category> categoryOptional = dao.findAllByNameEquals(nameCategory);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        } else {
            return null;
        }
    }

    @Override
    public Category findById(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    public Category save(Category category) {
        return dao.save(category);
    }
    
    @Override
    public void delete(Category categoryToDelete) {
        List<Product> products = productService.All();
        for (Product product: products){
            Long categoryId = categoryToDelete.getId();
            if(product.getCategory() != null && product.getCategory().getId() == categoryId){
                product.setCategory(null);
            }
        }
        dao.delete(categoryToDelete);
    }
}
