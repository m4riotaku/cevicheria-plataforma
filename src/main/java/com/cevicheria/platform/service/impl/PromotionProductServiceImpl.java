package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.PromotionProduct;
import com.cevicheria.platform.model.id.PromotionProductId;
import com.cevicheria.platform.repository.PromotionProductRepository;
import com.cevicheria.platform.service.PromotionProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionProductServiceImpl implements PromotionProductService {

    private final PromotionProductRepository promotionProductRepository;

    @Override
    public List<PromotionProduct> getAllByBusiness(Long businessId) {
        return promotionProductRepository.findByBusiness_Id(businessId);
    }

    @Override
    public List<PromotionProduct> getByPromotion(Long businessId, Long promotionId) {
        return promotionProductRepository.findByBusiness_IdAndPromotion_Id(businessId, promotionId);
    }

    @Override
    public List<PromotionProduct> getByProduct(Long businessId, Long productId) {
        return promotionProductRepository.findByBusiness_IdAndProduct_Id(businessId, productId);
    }

    @Override
    public PromotionProduct getById(PromotionProductId id) {
        return promotionProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PromotionProduct", id.getPromotionId()));
    }

    @Override
    public PromotionProduct save(PromotionProduct promotionProduct) {
        return promotionProductRepository.save(promotionProduct);
    }

    @Override
    public void deleteById(PromotionProductId id) {
        promotionProductRepository.delete(getById(id));
    }
}