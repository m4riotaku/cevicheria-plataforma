package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.RecipeIngredient;

public interface RecipeIngredientService {

    List<RecipeIngredient> getAllByBusiness(Long businessId);

    List<RecipeIngredient> getByRecipe(Long businessId, Long recipeId);

    RecipeIngredient getById(Long id);

    RecipeIngredient save(RecipeIngredient ingredient);

    RecipeIngredient update(Long id, RecipeIngredient ingredient);

    void deleteById(Long id);
}