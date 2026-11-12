package com.cevicheria.platform.controller;

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

import com.cevicheria.platform.dto.BusinessConfigurationRequest;
import com.cevicheria.platform.dto.BusinessConfigurationResponse;
import com.cevicheria.platform.model.BusinessConfiguration;
import com.cevicheria.platform.model.enums.InvoiceEnvironment;
import com.cevicheria.platform.service.BusinessConfigurationService;
import com.cevicheria.platform.service.BusinessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/business-configurations")
@RequiredArgsConstructor
public class BusinessConfigurationController {

    private final BusinessConfigurationService businessConfigurationService;
    private final BusinessService businessService;

    @GetMapping("/{businessId}")
    public BusinessConfigurationResponse getById(
            @PathVariable Long businessId) {
        return toResponse(businessConfigurationService.getById(businessId));
    }

    @PostMapping
    public ResponseEntity<BusinessConfigurationResponse> create(
            @RequestParam Long businessId,
            @RequestBody BusinessConfigurationRequest request) {
        BusinessConfiguration value = toModel(businessId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(businessConfigurationService.save(value)));
    }

    @PutMapping("/{businessId}")
    public BusinessConfigurationResponse update(
            @PathVariable Long businessId,
            @RequestBody BusinessConfigurationRequest request) {
        BusinessConfiguration value = toModel(businessId, request);
        return toResponse(businessConfigurationService.update(businessId, value));
    }

    @DeleteMapping("/{businessId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long businessId) {
        businessConfigurationService.deleteById(businessId);
        return ResponseEntity.noContent().build();
    }

    private BusinessConfiguration toModel(Long businessId, BusinessConfigurationRequest request) {
        InvoiceEnvironment environment = request.invoiceEnvironment() == null
                ? InvoiceEnvironment.PRUEBAS : request.invoiceEnvironment();
        BusinessConfiguration value = new BusinessConfiguration(
                businessService.getById(businessId), environment, request.billingProvider());
        if (Boolean.TRUE.equals(request.reservationsEnabled())) {
            value.enableReservations();
        }
        return value;
    }

    private static BusinessConfigurationResponse toResponse(BusinessConfiguration value) {
        return new BusinessConfigurationResponse(
                value.getBusinessId(),
                value.isReservationsEnabled(),
                value.getInvoiceEnvironment(),
                value.getBillingProvider(),
                value.getCreatedAt());
    }
}
