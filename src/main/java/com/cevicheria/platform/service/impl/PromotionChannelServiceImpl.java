package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.PromotionChannel;
import com.cevicheria.platform.model.id.PromotionChannelId;
import com.cevicheria.platform.repository.PromotionChannelRepository;
import com.cevicheria.platform.service.PromotionChannelService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionChannelServiceImpl implements PromotionChannelService {

    private final PromotionChannelRepository promotionChannelRepository;

    @Override
    public List<PromotionChannel> getAllByBusiness(Long businessId) {
        return promotionChannelRepository.findByBusiness_Id(businessId);
    }

    @Override
    public List<PromotionChannel> getByPromotion(Long businessId, Long promotionId) {
        return promotionChannelRepository.findByBusiness_IdAndPromotion_Id(businessId, promotionId);
    }

    @Override
    public PromotionChannel getById(PromotionChannelId id) {
        return promotionChannelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PromotionChannel", id.getPromotionId()));
    }

    @Override
    public PromotionChannel save(PromotionChannel promotionChannel) {
        return promotionChannelRepository.save(promotionChannel);
    }

    @Override
    public void deleteById(PromotionChannelId id) {
        promotionChannelRepository.delete(getById(id));
    }
}