package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.BranchProduct;
import com.cevicheria.platform.model.enums.SalesChannel;

public interface BranchProductService {

    List<BranchProduct> getAllByBusiness(Long businessId);

    List<BranchProduct> getByBranch(Long businessId, Long branchId);

    List<BranchProduct> getByProduct(Long businessId, Long productId);

        List<BranchProduct> getVisibleByBranch(Long businessId, Long branchId);

        List<BranchProduct> getAvailableByBranch(Long businessId, Long branchId);

        BranchProduct getByBranchProductAndChannel(Long businessId, Long branchId, Long productId,
            SalesChannel salesChannel);

    BranchProduct getById(Long id);

    BranchProduct save(BranchProduct branchProduct);

    BranchProduct update(Long id, BranchProduct branchProduct);

    void deleteById(Long id);
}