package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.PromotionChannel;
import com.cevicheria.platform.model.id.PromotionChannelId;

public interface PromotionChannelRepository extends JpaRepository<PromotionChannel, PromotionChannelId> {

    List<PromotionChannel> findByBusinessId(Long businessId);

    List<PromotionChannel> findByBusinessIdAndPromotionId(Long businessId, Long promotionId);

    boolean existsByBusinessIdAndPromotionIdAndIdSalesChannel(
            Long businessId, Long promotionId, com.cevicheria.platform.model.enums.SalesChannel salesChannel);
}