package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.PromotionChannel;
import com.cevicheria.platform.model.id.PromotionChannelId;

public interface PromotionChannelRepository extends JpaRepository<PromotionChannel, PromotionChannelId> {

    List<PromotionChannel> findByBusiness_Id(Long businessId);

    List<PromotionChannel> findByBusiness_IdAndPromotion_Id(Long businessId, Long promotionId);
}