package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.PromotionBranch;
import com.cevicheria.platform.model.id.PromotionBranchId;

public interface PromotionBranchService {

    List<PromotionBranch> getAllByBusiness(Long businessId);

    List<PromotionBranch> getByPromotion(Long businessId, Long promotionId);

    PromotionBranch getById(PromotionBranchId id);

    PromotionBranch save(PromotionBranch promotionBranch);

    void deleteById(PromotionBranchId id);
}