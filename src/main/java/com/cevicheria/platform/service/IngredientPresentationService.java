package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.IngredientPresentation;

public interface IngredientPresentationService {

    List<IngredientPresentation> getAllByBusiness(Long businessId);

    List<IngredientPresentation> getByIngredient(Long businessId, Long ingredientId);

    List<IngredientPresentation> getActiveByIngredient(Long businessId, Long ingredientId);

    IngredientPresentation getById(Long id);

    IngredientPresentation save(IngredientPresentation presentation);

    IngredientPresentation update(Long id, IngredientPresentation presentation);

    void deleteById(Long id);
}