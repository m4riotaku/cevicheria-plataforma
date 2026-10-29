package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.IngredientPresentation;
import com.cevicheria.platform.repository.IngredientPresentationRepository;
import com.cevicheria.platform.service.IngredientPresentationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredientPresentationServiceImpl implements IngredientPresentationService {

    private final IngredientPresentationRepository presentationRepository;

    @Override
    public List<IngredientPresentation> getAllByBusiness(Long businessId) {
        return presentationRepository.findByBusinessId(businessId);
    }

    @Override
    public List<IngredientPresentation> getByIngredient(Long businessId, Long ingredientId) {
        return presentationRepository.findByBusinessIdAndIngredientId(businessId, ingredientId);
    }

    @Override
    public List<IngredientPresentation> getActiveByIngredient(Long businessId, Long ingredientId) {
        return presentationRepository.findByBusinessIdAndIngredientIdAndActiveTrue(businessId, ingredientId);
    }

    @Override
    public IngredientPresentation getById(Long id) {
        return presentationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IngredientPresentation", "ID", id));
    }

    @Override
    public IngredientPresentation save(IngredientPresentation presentation) {
        return presentationRepository.save(presentation);
    }

    @Override
    public IngredientPresentation update(Long id, IngredientPresentation presentation) {
        IngredientPresentation current = getById(id);
        if (presentation.getName() != null) {
            current.changeName(presentation.getName());
        }
        if (presentation.getType() != null) {
            current.changeType(presentation.getType());
        }
        if (presentation.getBaseConversionFactor() != null) {
            current.changeBaseConversionFactor(presentation.getBaseConversionFactor());
        }
        if (presentation.getBaseTare() != null) {
            current.changeBaseTare(presentation.getBaseTare());
        }
        if (presentation.isActive()) {
            current.activate();
        } else {
            current.deactivate();
        }
        return presentationRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        presentationRepository.delete(getById(id));
    }
}