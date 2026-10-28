package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.BranchProduct;

public interface BranchProductService {

    List<BranchProduct> getAllByBusiness(Long businessId);

    List<BranchProduct> getByBranch(Long businessId, Long branchId);

    List<BranchProduct> getByProduct(Long businessId, Long productId);

    BranchProduct getById(Long id);

    BranchProduct save(BranchProduct branchProduct);

    BranchProduct update(Long id, BranchProduct branchProduct);

    void deleteById(Long id);
}