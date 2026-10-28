package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.PromotionBranch;
import com.cevicheria.platform.model.id.PromotionBranchId;
import com.cevicheria.platform.repository.PromotionBranchRepository;
import com.cevicheria.platform.service.PromotionBranchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionBranchServiceImpl implements PromotionBranchService {

    private final PromotionBranchRepository promotionBranchRepository;

    @Override
    public List<PromotionBranch> getAllByBusiness(Long businessId) {
        return promotionBranchRepository.findByBusiness_Id(businessId);
    }

    @Override
    public List<PromotionBranch> getByPromotion(Long businessId, Long promotionId) {
        return promotionBranchRepository.findByBusiness_IdAndPromotion_Id(businessId, promotionId);
    }

    @Override
    public PromotionBranch getById(PromotionBranchId id) {
        return promotionBranchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PromotionBranch", id.getPromotionId()));
    }

    @Override
    public PromotionBranch save(PromotionBranch promotionBranch) {
        return promotionBranchRepository.save(promotionBranch);
    }

    @Override
    public void deleteById(PromotionBranchId id) {
        promotionBranchRepository.delete(getById(id));
    }
}