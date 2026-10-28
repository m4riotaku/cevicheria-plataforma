package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.RecipeIngredient;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

	List<RecipeIngredient> findByBusiness_Id(Long businessId);

	List<RecipeIngredient> findByBusiness_IdAndRecipe_Id(Long businessId, Long recipeId);

	List<RecipeIngredient> findByBusiness_IdAndIngredient_Id(Long businessId, Long ingredientId);
}
