package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.BranchProduct;

public interface BranchProductRepository extends JpaRepository<BranchProduct, Long> {

	List<BranchProduct> findByBusiness_Id(Long businessId);

	List<BranchProduct> findByBusiness_IdAndBranch_Id(Long businessId, Long branchId);

	List<BranchProduct> findByBusiness_IdAndProduct_Id(Long businessId, Long productId);

	boolean existsByBusiness_IdAndBranch_IdAndProduct_IdAndSalesChannel(
			Long businessId, Long branchId, Long productId, com.cevicheria.platform.model.enums.SalesChannel salesChannel);
}
