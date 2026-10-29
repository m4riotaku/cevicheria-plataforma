package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Recipe;
import com.cevicheria.platform.repository.RecipeRepository;
import com.cevicheria.platform.service.RecipeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Override
    public List<Recipe> getAllByBusiness(Long businessId) {
        return recipeRepository.findByBusinessId(businessId);
    }

    @Override
    public List<Recipe> getByProduct(Long businessId, Long productId) {
        return recipeRepository.findByBusinessIdAndProductId(businessId, productId);
    }

    @Override
    public List<Recipe> getActiveByProduct(Long businessId, Long productId) {
        return recipeRepository.findByBusinessIdAndProductIdAndActiveTrue(businessId, productId);
    }

    @Override
    public Recipe getLatestActiveByProduct(Long businessId, Long productId) {
        return recipeRepository.findFirstByBusinessIdAndProductIdAndActiveTrueOrderByVersionDesc(
                businessId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "productId", productId));
    }

    @Override
    public Recipe getById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "ID", id));
    }

    @Override
    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe update(Long id, Recipe recipe) {
        Recipe current = getById(id);
        if (recipe.getVersion() != null) {
            current.changeVersion(recipe.getVersion());
        }
        if (recipe.getServings() != null) {
            current.changeServings(recipe.getServings());
        }
        if (recipe.getYieldRatio() != null) {
            current.changeYieldRatio(recipe.getYieldRatio());
        }
        current.changeInstructions(recipe.getInstructions());
        if (recipe.isActive()) {
            current.activate();
        } else {
            current.deactivate();
        }
        return recipeRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.delete(getById(id));
    }
}