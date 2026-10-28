package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.PromotionProduct;
import com.cevicheria.platform.model.id.PromotionProductId;

public interface PromotionProductRepository extends JpaRepository<PromotionProduct, PromotionProductId> {

    List<PromotionProduct> findByBusiness_Id(Long businessId);

    List<PromotionProduct> findByBusiness_IdAndPromotion_Id(Long businessId, Long promotionId);

    List<PromotionProduct> findByBusiness_IdAndProduct_Id(Long businessId, Long productId);
}