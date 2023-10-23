package com.ideaas.ecomm.ecomm.services.interfaces;



import java.util.List;

import com.ideaas.ecomm.ecomm.domain.Recipe;

public interface IRecipeService {

    List<Recipe> findAll();

    Recipe save(Recipe recipe);

    Recipe findById(Long id);

    void deleteById(Long id);

    Recipe update(Recipe recipe, Long id);
    
}
