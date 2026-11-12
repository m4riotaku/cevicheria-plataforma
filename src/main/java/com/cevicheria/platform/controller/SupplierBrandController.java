package com.cevicheria.platform.controller;
import com.cevicheria.platform.dto.SupplierBrandRequest;
import com.cevicheria.platform.dto.SupplierBrandResponse;
import com.cevicheria.platform.service.SupplierBrandService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supplier-brands")
public class SupplierBrandController {
    private final SupplierBrandService service;

    @GetMapping
    public List<SupplierBrandResponse> getAll(@RequestParam Long businessId) {
        return service.getAll(businessId);
    }

    @GetMapping("/{id}")
    public SupplierBrandResponse getById(@RequestParam Long businessId, @PathVariable Long id) {
        return service.getById(businessId, id);
    }

    @PostMapping
    public ResponseEntity<SupplierBrandResponse> create(@RequestBody SupplierBrandRequest request) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    @PatchMapping("/{id}")
    public SupplierBrandResponse update(@RequestParam Long businessId, @PathVariable Long id,
                              @RequestBody SupplierBrandRequest request) {
        return service.update(businessId, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestParam Long businessId, @PathVariable Long id) {
        service.delete(businessId, id);
        return ResponseEntity.noContent().build();
    }
}
