package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.RecipeIngredient;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

	List<RecipeIngredient> findByBusinessId(Long businessId);

	List<RecipeIngredient> findByBusinessIdAndRecipeId(Long businessId, Long recipeId);

	List<RecipeIngredient> findByBusinessIdAndIngredientId(Long businessId, Long ingredientId);
}
