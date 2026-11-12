package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.ModifierIngredient;
import com.cevicheria.platform.repository.ModifierIngredientRepository;
import com.cevicheria.platform.service.ModifierIngredientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModifierIngredientServiceImpl implements ModifierIngredientService {

    private final ModifierIngredientRepository modifierIngredientRepository;

    @Override
    public List<ModifierIngredient> getAllByBusiness(Long businessId) {
        return modifierIngredientRepository.findByBusinessId(businessId);
    }

    @Override
    public List<ModifierIngredient> getByModifier(Long businessId, Long modifierId) {
        return modifierIngredientRepository.findByBusinessIdAndModifierId(businessId, modifierId);
    }

    @Override
    public List<ModifierIngredient> getByIngredient(Long businessId, Long ingredientId) {
        return modifierIngredientRepository.findByBusinessIdAndIngredientId(businessId, ingredientId);
    }

    @Override
    public ModifierIngredient getById(Long id) {
        return modifierIngredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ModifierIngredient", "ID", id));
    }

    @Override
    public ModifierIngredient save(ModifierIngredient ingredient) {
        return modifierIngredientRepository.save(ingredient);
    }

    @Override
    public ModifierIngredient update(Long id, ModifierIngredient ingredient) {
        ModifierIngredient current = getById(id);
        if (ingredient.getBaseQuantity() != null) {
            current.changeBaseQuantity(ingredient.getBaseQuantity());
        }
        return modifierIngredientRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        modifierIngredientRepository.delete(getById(id));
    }
}