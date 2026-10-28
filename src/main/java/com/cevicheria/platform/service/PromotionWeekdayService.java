package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.PromotionWeekday;
import com.cevicheria.platform.model.id.PromotionWeekdayId;

public interface PromotionWeekdayService {

    List<PromotionWeekday> getAllByBusiness(Long businessId);

    List<PromotionWeekday> getByPromotion(Long businessId, Long promotionId);

    PromotionWeekday getById(PromotionWeekdayId id);

    PromotionWeekday save(PromotionWeekday promotionWeekday);

    void deleteById(PromotionWeekdayId id);
}