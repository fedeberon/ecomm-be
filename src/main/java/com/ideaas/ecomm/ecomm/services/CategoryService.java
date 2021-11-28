package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.repository.CategoryDao;
import com.ideaas.ecomm.ecomm.services.interfaces.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    private CategoryDao dao;

    @Autowired
    public CategoryService(CategoryDao dao) {
        this.dao = dao;
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
}
