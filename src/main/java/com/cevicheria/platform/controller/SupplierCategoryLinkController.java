package com.cevicheria.platform.controller;
import com.cevicheria.platform.dto.SupplierCategoryLinkResponse;
import com.cevicheria.platform.service.SupplierCategoryLinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suppliers/{supplierId}/categories")
public class SupplierCategoryLinkController {
    private final SupplierCategoryLinkService service;

    @GetMapping
    public List<SupplierCategoryLinkResponse> getAll(@RequestParam Long businessId, @PathVariable Long supplierId) {
        return service.getAll(businessId, supplierId);
    }

    @PostMapping("/{targetId}")
    public ResponseEntity<SupplierCategoryLinkResponse> create(@RequestParam Long businessId,
            @PathVariable Long supplierId, @PathVariable Long targetId) {
        return ResponseEntity.status(201).body(service.create(businessId, supplierId, targetId));
    }

    @DeleteMapping("/{targetId}")
    public ResponseEntity<Void> delete(@RequestParam Long businessId,
            @PathVariable Long supplierId, @PathVariable Long targetId) {
        service.delete(businessId, supplierId, targetId);
        return ResponseEntity.noContent().build();
    }
}
