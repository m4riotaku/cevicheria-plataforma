package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.PromotionBranch;
import com.cevicheria.platform.model.id.PromotionBranchId;

public interface PromotionBranchRepository extends JpaRepository<PromotionBranch, PromotionBranchId> {

    List<PromotionBranch> findByBusiness_Id(Long businessId);

    List<PromotionBranch> findByBusiness_IdAndPromotion_Id(Long businessId, Long promotionId);

    List<PromotionBranch> findByBusiness_IdAndBranch_Id(Long businessId, Long branchId);
}