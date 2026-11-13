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
import com.cevicheria.platform.dto.SupplierIngredientRequest;
import com.cevicheria.platform.dto.SupplierIngredientResponse;
import com.cevicheria.platform.service.SupplierIngredientService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supplier-ingredients")
public class SupplierIngredientController {
    private final SupplierIngredientService service;

    @GetMapping
    public List<SupplierIngredientResponse> getAll(@RequestParam Long businessId) {
        return service.getAll(businessId);
    }

    @GetMapping("/{id}")
    public SupplierIngredientResponse getById(@RequestParam Long businessId, @PathVariable Long id) {
        return service.getById(businessId, id);
    }

    @PostMapping
    public ResponseEntity<SupplierIngredientResponse> create(@RequestBody SupplierIngredientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public SupplierIngredientResponse update(@RequestParam Long businessId, @PathVariable Long id,
                                             @RequestBody SupplierIngredientRequest request) {
        return service.update(businessId, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestParam Long businessId, @PathVariable Long id) {
        service.delete(businessId, id);
        return ResponseEntity.noContent().build();
    }
}
