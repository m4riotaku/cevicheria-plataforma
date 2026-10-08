package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.RecipeIngredient;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

}
