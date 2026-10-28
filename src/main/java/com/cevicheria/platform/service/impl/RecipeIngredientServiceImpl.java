package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.RecipeIngredient;
import com.cevicheria.platform.repository.RecipeIngredientRepository;
import com.cevicheria.platform.service.RecipeIngredientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeIngredientServiceImpl implements RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;

    @Override
    public List<RecipeIngredient> getAllByBusiness(Long businessId) {
        return recipeIngredientRepository.findByBusinessId(businessId);
    }

    @Override
    public List<RecipeIngredient> getByRecipe(Long businessId, Long recipeId) {
        return recipeIngredientRepository.findByBusinessIdAndRecipeId(businessId, recipeId);
    }

    @Override
    public List<RecipeIngredient> getByIngredient(Long businessId, Long ingredientId) {
        return recipeIngredientRepository.findByBusinessIdAndIngredientId(businessId, ingredientId);
    }

    @Override
    public RecipeIngredient getById(Long id) {
        return recipeIngredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RecipeIngredient", id));
    }

    @Override
    public RecipeIngredient save(RecipeIngredient ingredient) {
        return recipeIngredientRepository.save(ingredient);
    }

    @Override
    public RecipeIngredient update(Long id, RecipeIngredient ingredient) {
        RecipeIngredient current = getById(id);
        if (ingredient.getBaseQuantity() != null) {
            current.changeBaseQuantity(ingredient.getBaseQuantity());
        }
        return recipeIngredientRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        recipeIngredientRepository.delete(getById(id));
    }
}