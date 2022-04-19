package com.ideaas.ecomm.ecomm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.services.interfaces.ICategoryService;

@RestController
@RequestMapping("category")
public class CategoryController {
    

    private ICategoryService categoryService;

    @Autowired
    public CategoryController(final ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping 
    private ResponseEntity<List<Category>> findAll() {
        List<Category> categories = categoryService.findAll();
        
        return ResponseEntity.ok(categories);
    }

}
