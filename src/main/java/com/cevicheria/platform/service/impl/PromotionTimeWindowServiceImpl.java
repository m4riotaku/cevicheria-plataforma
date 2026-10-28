package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.PromotionTimeWindow;
import com.cevicheria.platform.model.id.PromotionTimeWindowId;
import com.cevicheria.platform.repository.PromotionTimeWindowRepository;
import com.cevicheria.platform.service.PromotionTimeWindowService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionTimeWindowServiceImpl implements PromotionTimeWindowService {

    private final PromotionTimeWindowRepository promotionTimeWindowRepository;

    @Override
    public List<PromotionTimeWindow> getAllByBusiness(Long businessId) {
        return promotionTimeWindowRepository.findByBusinessId(businessId);
    }

    @Override
    public List<PromotionTimeWindow> getByPromotion(Long businessId, Long promotionId) {
        return promotionTimeWindowRepository.findByBusinessIdAndPromotionId(businessId, promotionId);
    }

    @Override
    public boolean existsForPromotionAndSequence(Long businessId, Long promotionId, Integer sequenceNumber) {
        return promotionTimeWindowRepository.existsByBusinessIdAndPromotionIdAndIdSequenceNumber(
                businessId, promotionId, sequenceNumber);
    }

    @Override
    public PromotionTimeWindow getById(PromotionTimeWindowId id) {
        return promotionTimeWindowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PromotionTimeWindow", id.getPromotionId()));
    }

    @Override
    public PromotionTimeWindow save(PromotionTimeWindow promotionTimeWindow) {
        return promotionTimeWindowRepository.save(promotionTimeWindow);
    }

    @Override
    public void deleteById(PromotionTimeWindowId id) {
        promotionTimeWindowRepository.delete(getById(id));
    }
}