package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.PromotionProduct;
import com.cevicheria.platform.model.id.PromotionProductId;

public interface PromotionProductRepository extends JpaRepository<PromotionProduct, PromotionProductId> {

    List<PromotionProduct> findByBusinessId(Long businessId);

    List<PromotionProduct> findByBusinessIdAndPromotionId(Long businessId, Long promotionId);

    List<PromotionProduct> findByBusinessIdAndProductId(Long businessId, Long productId);

    boolean existsByBusinessIdAndPromotionIdAndProductId(Long businessId, Long promotionId, Long productId);
}