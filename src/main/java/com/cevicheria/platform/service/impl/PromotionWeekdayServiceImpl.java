package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.PromotionWeekday;
import com.cevicheria.platform.model.id.PromotionWeekdayId;
import com.cevicheria.platform.repository.PromotionWeekdayRepository;
import com.cevicheria.platform.service.PromotionWeekdayService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionWeekdayServiceImpl implements PromotionWeekdayService {

    private final PromotionWeekdayRepository promotionWeekdayRepository;

    @Override
    public List<PromotionWeekday> getAllByBusiness(Long businessId) {
        return promotionWeekdayRepository.findByBusinessId(businessId);
    }

    @Override
    public List<PromotionWeekday> getByPromotion(Long businessId, Long promotionId) {
        return promotionWeekdayRepository.findByBusinessIdAndPromotionId(businessId, promotionId);
    }

    @Override
    public boolean existsForPromotionAndWeekday(Long businessId, Long promotionId, Integer weekday) {
        return promotionWeekdayRepository.existsByBusinessIdAndPromotionIdAndIdWeekday(
                businessId, promotionId, weekday);
    }

    @Override
    public PromotionWeekday getById(PromotionWeekdayId id) {
        return promotionWeekdayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PromotionWeekday", "ID", id));
    }

    @Override
    public PromotionWeekday save(PromotionWeekday promotionWeekday) {
        return promotionWeekdayRepository.save(promotionWeekday);
    }

    @Override
    public void deleteById(PromotionWeekdayId id) {
        promotionWeekdayRepository.delete(getById(id));
    }
}