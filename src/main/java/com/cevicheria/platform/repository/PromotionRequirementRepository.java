package com.cevicheria.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.PromotionRequirement;
import com.cevicheria.platform.model.id.PromotionRequirementId;

public interface PromotionRequirementRepository extends JpaRepository<PromotionRequirement, PromotionRequirementId> {

    List<PromotionRequirement> findByBusinessId(Long businessId);

    Optional<PromotionRequirement> findByBusinessIdAndPromotionId(Long businessId, Long promotionId);
}