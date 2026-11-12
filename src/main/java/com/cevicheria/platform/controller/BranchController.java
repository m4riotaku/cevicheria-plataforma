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

import com.cevicheria.platform.dto.BranchRequest;
import com.cevicheria.platform.dto.BranchResponse;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.service.BranchService;
import com.cevicheria.platform.service.BusinessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;
    private final BusinessService businessService;

    @GetMapping
    public List<BranchResponse> getAll(@RequestParam Long businessId) {
        return branchService.getAllByBusiness(businessId).stream()
                .map(BranchController::toResponse)
                .toList();
    }

    @GetMapping("/active")
    public List<BranchResponse> getActive(@RequestParam Long businessId) {
        return branchService.getActiveByBusiness(businessId).stream()
                .map(BranchController::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public BranchResponse getById(
            @RequestParam Long businessId,
            @PathVariable Long id) {
        return toResponse(branchService.getById(businessId, id));
    }

    @PostMapping
    public ResponseEntity<BranchResponse> create(
            @RequestParam Long businessId,
            @RequestBody BranchRequest request) {
        Branch value = toModel(businessId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(branchService.save(value)));
    }

    @PutMapping("/{id}")
    public BranchResponse update(
            @RequestParam Long businessId,
            @PathVariable Long id,
            @RequestBody BranchRequest request) {
        Branch value = toModel(businessId, request);
        return toResponse(branchService.update(businessId, id, value));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestParam Long businessId,
            @PathVariable Long id) {
        branchService.deleteById(businessId, id);
        return ResponseEntity.noContent().build();
    }

    private Branch toModel(Long businessId, BranchRequest request) {
        Branch value = new Branch(businessService.getById(businessId), request.name(), request.address());
        value.changeUbigeoCode(request.ubigeoCode());
        value.changeDepartment(request.department());
        value.changeProvince(request.province());
        value.changeDistrict(request.district());
        value.changePhone(request.phone());
        value.changeMenuSlug(request.menuSlug());
        if (request.timezone() != null) {
            value.changeTimezone(request.timezone());
        }
        if (request.responsibleEmployeeId() != null) {
            value.changeResponsibleEmployee(branchService.getResponsibleEmployee(businessId, request.responsibleEmployeeId()));
        }
        if (Boolean.FALSE.equals(request.active())) {
            value.deactivate();
        }
        if (Boolean.TRUE.equals(request.deliveryEnabled())) {
            value.enableDelivery();
        }
        if (Boolean.FALSE.equals(request.dineInEnabled())) {
            value.disableDineIn();
        }
        if (Boolean.FALSE.equals(request.takeoutEnabled())) {
            value.disableTakeout();
        }
        if (Boolean.TRUE.equals(request.reservationsEnabled())) {
            value.enableReservations();
        }
        if (Boolean.TRUE.equals(request.publicMenuEnabled())) {
            value.enablePublicMenu();
        }
        return value;
    }

    private static BranchResponse toResponse(Branch value) {
        Long employeeId = value.getResponsibleEmployee() == null ? null : value.getResponsibleEmployee().getId();
        return new BranchResponse(
                value.getId(),
                value.getBusiness().getId(),
                value.getName(),
                value.getAddress(),
                value.isActive(),
                value.getUbigeoCode(),
                value.getDepartment(),
                value.getProvince(),
                value.getDistrict(),
                value.getPhone(),
                value.getTimezone(),
                value.isDineInEnabled(),
                value.isTakeoutEnabled(),
                value.isDeliveryEnabled(),
                value.isReservationsEnabled(),
                value.isPublicMenuEnabled(),
                value.getMenuSlug(),
                employeeId,
                value.getCreatedAt());
    }
}
