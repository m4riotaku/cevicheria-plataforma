package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.PromotionProduct;
import com.cevicheria.platform.model.id.PromotionProductId;

public interface PromotionProductService {

    List<PromotionProduct> getAllByBusiness(Long businessId);

    List<PromotionProduct> getByPromotion(Long businessId, Long promotionId);

    List<PromotionProduct> getByProduct(Long businessId, Long productId);

    boolean existsForPromotionAndProduct(Long businessId, Long promotionId, Long productId);

    PromotionProduct getById(PromotionProductId id);

    PromotionProduct save(PromotionProduct promotionProduct);

    void deleteById(PromotionProductId id);
}