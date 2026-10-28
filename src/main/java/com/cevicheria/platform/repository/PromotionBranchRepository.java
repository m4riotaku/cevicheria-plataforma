package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.PromotionBranch;
import com.cevicheria.platform.model.id.PromotionBranchId;

public interface PromotionBranchRepository extends JpaRepository<PromotionBranch, PromotionBranchId> {

    List<PromotionBranch> findByBusinessId(Long businessId);

    List<PromotionBranch> findByBusinessIdAndPromotionId(Long businessId, Long promotionId);

    List<PromotionBranch> findByBusinessIdAndBranchId(Long businessId, Long branchId);

    boolean existsByBusinessIdAndPromotionIdAndBranchId(Long businessId, Long promotionId, Long branchId);
}