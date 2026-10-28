package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.PromotionWeekday;
import com.cevicheria.platform.model.id.PromotionWeekdayId;

public interface PromotionWeekdayRepository extends JpaRepository<PromotionWeekday, PromotionWeekdayId> {

    List<PromotionWeekday> findByBusinessId(Long businessId);

    List<PromotionWeekday> findByBusinessIdAndPromotionId(Long businessId, Long promotionId);

    boolean existsByBusinessIdAndPromotionIdAndIdWeekday(Long businessId, Long promotionId, Integer weekday);
}