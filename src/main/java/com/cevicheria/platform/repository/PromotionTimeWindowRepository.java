package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.PromotionTimeWindow;
import com.cevicheria.platform.model.id.PromotionTimeWindowId;

public interface PromotionTimeWindowRepository extends JpaRepository<PromotionTimeWindow, PromotionTimeWindowId> {

    List<PromotionTimeWindow> findByBusinessId(Long businessId);

    List<PromotionTimeWindow> findByBusinessIdAndPromotionId(Long businessId, Long promotionId);

    boolean existsByBusinessIdAndPromotionIdAndIdSequenceNumber(
            Long businessId, Long promotionId, Integer sequenceNumber);
}