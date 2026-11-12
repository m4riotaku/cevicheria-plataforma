package com.cevicheria.platform.service;

import java.util.List;
import java.util.Optional;

import com.cevicheria.platform.model.PromotionRequirement;
import com.cevicheria.platform.model.id.PromotionRequirementId;

public interface PromotionRequirementService {

    List<PromotionRequirement> getAllByBusiness(Long businessId);

    Optional<PromotionRequirement> getByPromotion(Long businessId, Long promotionId);

    PromotionRequirement getById(PromotionRequirementId id);

    PromotionRequirement save(PromotionRequirement promotionRequirement);

    void deleteById(PromotionRequirementId id);
}