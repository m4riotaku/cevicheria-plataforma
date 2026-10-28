package com.cevicheria.platform.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.PromotionRequirement;
import com.cevicheria.platform.model.id.PromotionRequirementId;
import com.cevicheria.platform.repository.PromotionRequirementRepository;
import com.cevicheria.platform.service.PromotionRequirementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionRequirementServiceImpl implements PromotionRequirementService {

    private final PromotionRequirementRepository promotionRequirementRepository;

    @Override
    public List<PromotionRequirement> getAllByBusiness(Long businessId) {
        return promotionRequirementRepository.findByBusiness_Id(businessId);
    }

    @Override
    public Optional<PromotionRequirement> getByPromotion(Long businessId, Long promotionId) {
        return promotionRequirementRepository.findByBusiness_IdAndPromotion_Id(businessId, promotionId);
    }

    @Override
    public PromotionRequirement getById(PromotionRequirementId id) {
        return promotionRequirementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PromotionRequirement", id.getPromotionId()));
    }

    @Override
    public PromotionRequirement save(PromotionRequirement promotionRequirement) {
        return promotionRequirementRepository.save(promotionRequirement);
    }

    @Override
    public void deleteById(PromotionRequirementId id) {
        promotionRequirementRepository.delete(getById(id));
    }
}