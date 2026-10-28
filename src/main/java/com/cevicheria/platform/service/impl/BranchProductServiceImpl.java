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
        return branchProductRepository.findByBusiness_Id(businessId);
    }

    @Override
    public List<BranchProduct> getByBranch(Long businessId, Long branchId) {
        return branchProductRepository.findByBusiness_IdAndBranch_Id(businessId, branchId);
    }

    @Override
    public List<BranchProduct> getByProduct(Long businessId, Long productId) {
        return branchProductRepository.findByBusiness_IdAndProduct_Id(businessId, productId);
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