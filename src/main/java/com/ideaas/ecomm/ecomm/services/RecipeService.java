package com.ideaas.ecomm.ecomm.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ideaas.ecomm.ecomm.domain.Image;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.Recipe;
import com.ideaas.ecomm.ecomm.exception.FileStorageException;
import com.ideaas.ecomm.ecomm.repository.RecipeDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IRecipeService;

@Service
public class RecipeService implements IRecipeService {

    private FileService fileService;
    private RecipeDao dao;

    public RecipeService(final RecipeDao dao,
                         final FileService fileService) {
        this.dao = dao;
        this.fileService = fileService;
    }

    @Override
    public List<Recipe> findAll() {
        List<Recipe> recipes = dao.findAll();
        recipes.forEach(this::addImagesOnRecipe);

        return recipes;
    }

    @Override
    public Recipe save(Recipe recipe) {
        Recipe recipeSave = dao.save(recipe);
        return recipeSave;
    }

    @Override
    public Recipe findById(Long id) {
        Recipe optionalRecipe = dao.findById(id).orElse(null);
        addImagesOnRecipe(optionalRecipe);

        return optionalRecipe;
        // return dao.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        dao.deleteById(id);
    }

    @Override
    public Recipe update(Recipe recipe, Long id) {
        return dao.save(recipe);
    }

    public void addImagesOnRecipe(final Recipe recipe) {
        List<Image> images = fileService.readFiles(recipe.getId().toString());
        images.forEach(image -> {
            String path = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/file/download/")
                    .path(recipe.getId().toString())
                    .path(File.separator)
                    .path(image.getUrl())
                    .toUriString();
            image.setLink(path);
        });
        recipe.setImages(images);
    }
}
