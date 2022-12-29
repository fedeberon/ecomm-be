package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.domain.Product;

import java.util.List;

public interface ICategoryService {
    List<Category> findAll();

    Category findAllByNameEquals(String nameCategory);

    Category findById(Long id);

    Category save(Category category);

    void delete(Category categoryToDelete);
}
