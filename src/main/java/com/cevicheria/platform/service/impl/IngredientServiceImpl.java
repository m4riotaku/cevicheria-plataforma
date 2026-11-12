package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Ingredient;
import com.cevicheria.platform.repository.IngredientRepository;
import com.cevicheria.platform.service.IngredientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    @Override
    public List<Ingredient> getAllByBusiness(Long businessId) {
        return ingredientRepository.findByBusinessId(businessId);
    }

    @Override
    public List<Ingredient> getActiveByBusiness(Long businessId) {
        return ingredientRepository.findByBusinessIdAndActiveTrue(businessId);
    }

    @Override
    public List<Ingredient> getActiveByCategory(Long businessId, Long categoryId) {
        return ingredientRepository.findByBusinessIdAndCategoryIdAndActiveTrue(businessId, categoryId);
    }

    @Override
    public Ingredient getById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "ID", id));
    }

    @Override
    public Ingredient getByBusinessAndCode(Long businessId, String code) {
        return ingredientRepository.findByBusinessIdAndCodeIgnoreCase(businessId, code)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "businessId/code", businessId + "/" + code));
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @Override
    public Ingredient update(Long id, Ingredient ingredient) {
        Ingredient current = getById(id);
        if (ingredient.getName() != null) {
            current.changeName(ingredient.getName());
        }
        if (ingredient.getCode() != null) {
            current.changeCode(ingredient.getCode());
        }
        if (ingredient.getCategory() != null) {
            current.changeCategory(ingredient.getCategory());
        }
        if (ingredient.getBaseUnit() != null) {
            current.changeBaseUnit(ingredient.getBaseUnit());
        }
        if (ingredient.isActive()) {
            current.activate();
        } else {
            current.deactivate();
        }
        return ingredientRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        ingredientRepository.delete(getById(id));
    }
}