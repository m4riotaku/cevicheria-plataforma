package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.Ingredient;

public interface IngredientService {

    List<Ingredient> getAllByBusiness(Long businessId);

    List<Ingredient> getActiveByBusiness(Long businessId);

    List<Ingredient> getActiveByCategory(Long businessId, Long categoryId);

    Ingredient getById(Long id);

    Ingredient getByBusinessAndCode(Long businessId, String code);

    Ingredient save(Ingredient ingredient);

    Ingredient update(Long id, Ingredient ingredient);

    void deleteById(Long id);
}