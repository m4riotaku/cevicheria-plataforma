package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.PromotionTimeWindow;
import com.cevicheria.platform.model.id.PromotionTimeWindowId;

public interface PromotionTimeWindowService {

    List<PromotionTimeWindow> getAllByBusiness(Long businessId);

    List<PromotionTimeWindow> getByPromotion(Long businessId, Long promotionId);

    PromotionTimeWindow getById(PromotionTimeWindowId id);

    PromotionTimeWindow save(PromotionTimeWindow promotionTimeWindow);

    void deleteById(PromotionTimeWindowId id);
}