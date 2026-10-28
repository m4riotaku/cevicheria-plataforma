package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Promotion;
import com.cevicheria.platform.repository.PromotionRepository;
import com.cevicheria.platform.service.PromotionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    @Override
    public List<Promotion> getAllByBusiness(Long businessId) {
        return promotionRepository.findByBusiness_Id(businessId);
    }

    @Override
    public List<Promotion> getActiveByBusiness(Long businessId) {
        return promotionRepository.findByBusiness_IdAndActiveTrue(businessId);
    }

    @Override
    public Promotion getById(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion", id));
    }

    @Override
    public Promotion save(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    @Override
    public Promotion update(Long id, Promotion promotion) {
        Promotion current = getById(id);
        if (promotion.getName() != null) {
            current.changeName(promotion.getName());
        }
        if (promotion.getType() != null) {
            current.changeType(promotion.getType());
        }
        if (promotion.getValueAmount() != null) {
            current.changeValueAmount(promotion.getValueAmount());
        }
        if (promotion.getPriority() != null) {
            current.changePriority(promotion.getPriority());
        }
        current.changeValidUntil(promotion.getValidUntil());
        if (promotion.isStackable()) {
            current.enableStackable();
        } else {
            current.disableStackable();
        }
        if (promotion.isActive()) {
            current.activate();
        } else {
            current.deactivate();
        }
        return promotionRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        promotionRepository.delete(getById(id));
    }
}