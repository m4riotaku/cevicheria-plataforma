package com.cevicheria.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.PromotionRequirement;
import com.cevicheria.platform.model.id.PromotionRequirementId;

public interface PromotionRequirementRepository extends JpaRepository<PromotionRequirement, PromotionRequirementId> {

    List<PromotionRequirement> findByBusiness_Id(Long businessId);

    Optional<PromotionRequirement> findByBusiness_IdAndPromotion_Id(Long businessId, Long promotionId);
}