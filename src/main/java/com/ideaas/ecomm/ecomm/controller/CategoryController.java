package com.ideaas.ecomm.ecomm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.services.interfaces.ICategoryService;

@RestController
@RequestMapping("category")
@CrossOrigin
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

    @GetMapping("{id}")
    private ResponseEntity<Category> findByID(@PathVariable final Long id) {
        Category category = categoryService.findById(id);

        return ResponseEntity.ok(category);
    }

    @PostMapping
    private ResponseEntity<Category> save(@RequestBody Category category) {
        final Category savedCategory = categoryService.save(category);

        return ResponseEntity.ok(savedCategory);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable final Long id){
        final Category categoryToDelete = categoryService.findById(id);
        categoryService.delete(categoryToDelete);

        return ResponseEntity.accepted().body("Category deleted succesfully");
    }
}
