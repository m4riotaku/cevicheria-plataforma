package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Modifier;
import com.cevicheria.platform.repository.ModifierRepository;
import com.cevicheria.platform.service.ModifierService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModifierServiceImpl implements ModifierService {

    private final ModifierRepository modifierRepository;

    @Override
    public List<Modifier> getAllByBusiness(Long businessId) {
        return modifierRepository.findByBusinessId(businessId);
    }

    @Override
    public List<Modifier> getActiveByBusiness(Long businessId) {
        return modifierRepository.findByBusinessIdAndActiveTrue(businessId);
    }

    @Override
    public List<Modifier> getByProduct(Long businessId, Long productId) {
        return modifierRepository.findByBusinessIdAndProductId(businessId, productId);
    }

    @Override
    public List<Modifier> getActiveByProduct(Long businessId, Long productId) {
        return modifierRepository.findByBusinessIdAndProductIdAndActiveTrue(businessId, productId);
    }

    @Override
    public List<Modifier> getByCategory(Long businessId, Long categoryId) {
        return modifierRepository.findByBusinessIdAndCategoryId(businessId, categoryId);
    }

    @Override
    public List<Modifier> getActiveByCategory(Long businessId, Long categoryId) {
        return modifierRepository.findByBusinessIdAndCategoryIdAndActiveTrue(businessId, categoryId);
    }

    @Override
    public Modifier getById(Long id) {
        return modifierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Modifier", id));
    }

    @Override
    public Modifier save(Modifier modifier) {
        return modifierRepository.save(modifier);
    }

    @Override
    public Modifier update(Long id, Modifier modifier) {
        Modifier current = getById(id);
        if (modifier.getName() != null) {
            current.changeName(modifier.getName());
        }
        if (modifier.getCategory() != null) {
            current.changeCategory(modifier.getCategory());
        }
        if (modifier.getExtraPrice() != null) {
            current.changeExtraPrice(modifier.getExtraPrice());
        }
        if (modifier.isActive()) {
            current.activate();
        } else {
            current.deactivate();
        }
        return modifierRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        modifierRepository.delete(getById(id));
    }
}