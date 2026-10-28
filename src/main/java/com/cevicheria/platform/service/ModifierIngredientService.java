package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.ModifierIngredient;

public interface ModifierIngredientService {

    List<ModifierIngredient> getAllByBusiness(Long businessId);

    List<ModifierIngredient> getByModifier(Long businessId, Long modifierId);

    List<ModifierIngredient> getByIngredient(Long businessId, Long ingredientId);

    ModifierIngredient getById(Long id);

    ModifierIngredient save(ModifierIngredient ingredient);

    ModifierIngredient update(Long id, ModifierIngredient ingredient);

    void deleteById(Long id);
}