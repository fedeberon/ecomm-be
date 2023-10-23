package com.ideaas.ecomm.ecomm.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ideaas.ecomm.ecomm.domain.Recipe;
import com.ideaas.ecomm.ecomm.exception.NotFoundException;
import com.ideaas.ecomm.ecomm.services.FileService;
import com.ideaas.ecomm.ecomm.services.interfaces.IRecipeService;

@RestController
@RequestMapping("/recipes")
@CrossOrigin
public class RecipeController {
    
    @Autowired
    private IRecipeService recipeRepository;

    private FileService fileService;

    @GetMapping
    public ResponseEntity<List<Recipe>> listRecipes() {
        try{
            List<Recipe> recipe = recipeRepository.findAll();
            return ResponseEntity.ok(recipe);
        }catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        
    }

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody final MultipartFile file, @RequestBody Recipe recipe) {
        try{
        Recipe recipeSave = recipeRepository.save(recipe);
        fileService.storeFile(file,recipeSave.getId().toString());
        return ResponseEntity.ok(recipeSave);
        }catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        Recipe recipe = recipeRepository.findById(id);
        if (recipe != null) {
            return ResponseEntity.ok(recipe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        Recipe recipeUpdate = recipeRepository.update(recipe,id);
        return ResponseEntity.ok(recipeUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
