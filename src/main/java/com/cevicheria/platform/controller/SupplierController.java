package com.cevicheria.platform.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.cevicheria.platform.dto.SupplierRequest;
import com.cevicheria.platform.dto.SupplierResponse;
import com.cevicheria.platform.service.SupplierService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierService service;

    @GetMapping
    public List<SupplierResponse> getAll(@RequestParam Long businessId) {
        return service.getAll(businessId);
    }

    @GetMapping("/{id}")
    public SupplierResponse getById(@RequestParam Long businessId, @PathVariable Long id) {
        return service.getById(businessId, id);
    }

    @PostMapping
    public ResponseEntity<SupplierResponse> create(@RequestBody SupplierRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public SupplierResponse update(@RequestParam Long businessId, @PathVariable Long id,
                                   @RequestBody SupplierRequest request) {
        return service.update(businessId, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestParam Long businessId, @PathVariable Long id) {
        service.delete(businessId, id);
        return ResponseEntity.noContent().build();
    }
}
