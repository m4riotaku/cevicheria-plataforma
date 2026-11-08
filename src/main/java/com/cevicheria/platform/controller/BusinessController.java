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
import org.springframework.web.bind.annotation.RestController;

import com.cevicheria.platform.dto.BusinessRequest;
import com.cevicheria.platform.dto.BusinessResponse;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.service.BusinessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @GetMapping
    public List<BusinessResponse> getAll() {
        return businessService.getAll().stream()
                .map(BusinessController::toResponse)
                .toList();
    }

    @GetMapping("/active")
    public List<BusinessResponse> getActive() {
        return businessService.getActive().stream()
                .map(BusinessController::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public BusinessResponse getById(
            @PathVariable Long id) {
        return toResponse(businessService.getById(id));
    }

    @PostMapping
    public ResponseEntity<BusinessResponse> create(
            @RequestBody BusinessRequest request) {
        Business value = toModel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(businessService.save(value)));
    }

    @PutMapping("/{id}")
    public BusinessResponse update(
            @PathVariable Long id,
            @RequestBody BusinessRequest request) {
        Business value = toModel(request);
        return toResponse(businessService.update(id, value));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        businessService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private static Business toModel(BusinessRequest request) {
        Business value = new Business(request.name(), request.legalName(), request.ruc(), request.businessFormat());
        if (request.currencyCode() != null) {
            value.changeCurrencyCode(request.currencyCode());
        }
        if (Boolean.FALSE.equals(request.active())) {
            value.deactivate();
        }
        return value;
    }

    private static BusinessResponse toResponse(Business value) {
        return new BusinessResponse(
                value.getId(),
                value.getName(),
                value.getLegalName(),
                value.getRuc(),
                value.getCurrencyCode(),
                value.getBusinessFormat(),
                value.isActive(),
                value.getCreatedAt());
    }
}
