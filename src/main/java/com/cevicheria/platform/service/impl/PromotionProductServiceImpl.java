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
        return promotionProductRepository.findByBusinessId(businessId);
    }

    @Override
    public List<PromotionProduct> getByPromotion(Long businessId, Long promotionId) {
        return promotionProductRepository.findByBusinessIdAndPromotionId(businessId, promotionId);
    }

    @Override
    public List<PromotionProduct> getByProduct(Long businessId, Long productId) {
        return promotionProductRepository.findByBusinessIdAndProductId(businessId, productId);
    }

    @Override
    public boolean existsForPromotionAndProduct(Long businessId, Long promotionId, Long productId) {
        return promotionProductRepository.existsByBusinessIdAndPromotionIdAndProductId(
                businessId, promotionId, productId);
    }

    @Override
    public PromotionProduct getById(PromotionProductId id) {
        return promotionProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PromotionProduct", "ID", id));
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