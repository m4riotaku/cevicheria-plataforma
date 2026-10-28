package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.PromotionWeekday;
import com.cevicheria.platform.model.id.PromotionWeekdayId;

public interface PromotionWeekdayRepository extends JpaRepository<PromotionWeekday, PromotionWeekdayId> {

    List<PromotionWeekday> findByBusiness_Id(Long businessId);

    List<PromotionWeekday> findByBusiness_IdAndPromotion_Id(Long businessId, Long promotionId);
}