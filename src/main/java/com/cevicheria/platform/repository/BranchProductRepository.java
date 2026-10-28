package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.BranchProduct;

public interface BranchProductRepository extends JpaRepository<BranchProduct, Long> {

	List<BranchProduct> findByBusinessId(Long businessId);

	List<BranchProduct> findByBusinessIdAndBranchId(Long businessId, Long branchId);

	List<BranchProduct> findByBusinessIdAndProductId(Long businessId, Long productId);

	List<BranchProduct> findByBusinessIdAndBranchIdAndVisibleTrue(Long businessId, Long branchId);

	List<BranchProduct> findByBusinessIdAndBranchIdAndBlockedFalse(Long businessId, Long branchId);

	java.util.Optional<BranchProduct> findByBusinessIdAndBranchIdAndProductIdAndSalesChannel(
			Long businessId, Long branchId, Long productId, com.cevicheria.platform.model.enums.SalesChannel salesChannel);

	boolean existsByBusinessIdAndBranchIdAndProductIdAndSalesChannel(
			Long businessId, Long branchId, Long productId, com.cevicheria.platform.model.enums.SalesChannel salesChannel);
}
