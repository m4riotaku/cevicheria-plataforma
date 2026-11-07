package com.cevicheria.platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cevicheria.platform.dto.BranchConfigurationRequest;
import com.cevicheria.platform.dto.BranchConfigurationResponse;
import com.cevicheria.platform.model.BranchConfiguration;
import com.cevicheria.platform.model.id.BranchConfigurationId;
import com.cevicheria.platform.service.BranchConfigurationService;
import com.cevicheria.platform.service.BranchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/branch-configurations")
@RequiredArgsConstructor
public class BranchConfigurationController {

    private final BranchConfigurationService branchConfigurationService;
    private final BranchService branchService;

    @GetMapping
    public List<BranchConfigurationResponse> getAll(@RequestParam Long businessId) {
        return branchConfigurationService.getAllByBusiness(businessId).stream()
                .map(BranchConfigurationController::toResponse)
                .toList();
    }

    @GetMapping("/{branchId}")
    public BranchConfigurationResponse getById(
            @RequestParam Long businessId,
            @PathVariable Long branchId) {
        return toResponse(branchConfigurationService.getById(new BranchConfigurationId(businessId, branchId)));
    }

    @PostMapping
    public ResponseEntity<BranchConfigurationResponse> create(
            @RequestParam Long businessId,
            @RequestParam Long branchId,
            @RequestBody BranchConfigurationRequest request) {
        BranchConfiguration value = toModel(businessId, branchId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(branchConfigurationService.save(value)));
    }

    @PutMapping("/{branchId}")
    public BranchConfigurationResponse update(
            @RequestParam Long businessId,
            @PathVariable Long branchId,
            @RequestBody BranchConfigurationRequest request) {
        BranchConfiguration value = toModel(businessId, branchId, request);
        return toResponse(branchConfigurationService.update(new BranchConfigurationId(businessId, branchId), value));
    }

    @DeleteMapping("/{branchId}")
    public ResponseEntity<Void> delete(
            @RequestParam Long businessId,
            @PathVariable Long branchId) {
        branchConfigurationService.deleteById(new BranchConfigurationId(businessId, branchId));
        return ResponseEntity.noContent().build();
    }

    private BranchConfiguration toModel(Long businessId, Long branchId, BranchConfigurationRequest request) {
        var branch = branchService.getById(businessId, branchId);
        BranchConfiguration value = new BranchConfiguration(branch.getBusiness(), branch, request.expirationAlertDays());
        if (Boolean.TRUE.equals(request.saleByWeightEnabled())) {
            value.enableSaleByWeight();
        }
        if (Boolean.TRUE.equals(request.stockAlertEnabled())) {
            value.enableStockAlert();
        }
        return value;
    }

    private static BranchConfigurationResponse toResponse(BranchConfiguration value) {
        return new BranchConfigurationResponse(
                value.getId().getBusinessId(),
                value.getId().getBranchId(),
                value.isSaleByWeightEnabled(),
                value.isStockAlertEnabled(),
                value.getExpirationAlertDays(),
                value.getCreatedAt());
    }
}
