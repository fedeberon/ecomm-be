package com.ideaas.ecomm.ecomm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ideaas.ecomm.ecomm.domain.Recipe;

@Repository
public interface RecipeDao extends JpaRepository<Recipe,Long> {
    
}
