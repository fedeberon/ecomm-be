package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> findAll();

    Category findAllByNameEquals(String nameCategory);
}
