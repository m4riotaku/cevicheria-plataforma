package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.PromotionChannel;
import com.cevicheria.platform.model.id.PromotionChannelId;
import com.cevicheria.platform.model.enums.SalesChannel;

public interface PromotionChannelService {

    List<PromotionChannel> getAllByBusiness(Long businessId);

    List<PromotionChannel> getByPromotion(Long businessId, Long promotionId);

    boolean existsForPromotionAndChannel(Long businessId, Long promotionId, SalesChannel salesChannel);

    PromotionChannel getById(PromotionChannelId id);

    PromotionChannel save(PromotionChannel promotionChannel);

    void deleteById(PromotionChannelId id);
}