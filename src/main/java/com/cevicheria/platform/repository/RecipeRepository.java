package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
