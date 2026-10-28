package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.Recipe;

public interface RecipeService {

    List<Recipe> getAllByBusiness(Long businessId);

    List<Recipe> getByProduct(Long businessId, Long productId);

    Recipe getById(Long id);

    Recipe save(Recipe recipe);

    Recipe update(Long id, Recipe recipe);

    void deleteById(Long id);
}