package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.BranchProduct;
import com.cevicheria.platform.repository.BranchProductRepository;
import com.cevicheria.platform.service.BranchProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BranchProductServiceImpl implements BranchProductService {

    private final BranchProductRepository branchProductRepository;

    @Override
    public List<BranchProduct> getAllByBusiness(Long businessId) {
        return branchProductRepository.findByBusinessId(businessId);
    }

    @Override
    public List<BranchProduct> getByBranch(Long businessId, Long branchId) {
        return branchProductRepository.findByBusinessIdAndBranchId(businessId, branchId);
    }

    @Override
    public List<BranchProduct> getByProduct(Long businessId, Long productId) {
        return branchProductRepository.findByBusinessIdAndProductId(businessId, productId);
    }

    @Override
    public List<BranchProduct> getVisibleByBranch(Long businessId, Long branchId) {
        return branchProductRepository.findByBusinessIdAndBranchIdAndVisibleTrue(businessId, branchId);
    }

    @Override
    public List<BranchProduct> getAvailableByBranch(Long businessId, Long branchId) {
        return branchProductRepository.findByBusinessIdAndBranchIdAndBlockedFalse(businessId, branchId);
    }

    @Override
    public BranchProduct getByBranchProductAndChannel(Long businessId, Long branchId, Long productId,
            com.cevicheria.platform.model.enums.SalesChannel salesChannel) {
        return branchProductRepository.findByBusinessIdAndBranchIdAndProductIdAndSalesChannel(
                businessId, branchId, productId, salesChannel)
                .orElseThrow(() -> new ResourceNotFoundException("BranchProduct", productId));
    }

    @Override
    public BranchProduct getById(Long id) {
        return branchProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BranchProduct", id));
    }

    @Override
    public BranchProduct save(BranchProduct branchProduct) {
        return branchProductRepository.save(branchProduct);
    }

    @Override
    public BranchProduct update(Long id, BranchProduct branchProduct) {
        BranchProduct current = getById(id);
        current.changePrice(branchProduct.getPrice());
        current.changeTaxTreatmentCode(branchProduct.getTaxTreatmentCode());
        if (branchProduct.isVisible()) {
            current.enableVisible();
        } else {
            current.disableVisible();
        }
        if (branchProduct.isBlocked()) {
            current.block(branchProduct.getBlockReason());
        } else {
            current.unblock();
        }
        current.changePreparationArea(branchProduct.getPreparationArea());
        return branchProductRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        branchProductRepository.delete(getById(id));
    }
}