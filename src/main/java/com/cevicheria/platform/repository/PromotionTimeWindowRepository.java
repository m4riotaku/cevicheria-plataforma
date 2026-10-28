package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.PromotionTimeWindow;
import com.cevicheria.platform.model.id.PromotionTimeWindowId;

public interface PromotionTimeWindowRepository extends JpaRepository<PromotionTimeWindow, PromotionTimeWindowId> {

    List<PromotionTimeWindow> findByBusiness_Id(Long businessId);

    List<PromotionTimeWindow> findByBusiness_IdAndPromotion_Id(Long businessId, Long promotionId);
}