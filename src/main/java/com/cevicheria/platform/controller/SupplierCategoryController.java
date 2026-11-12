package com.cevicheria.platform.controller;
import com.cevicheria.platform.dto.SupplierCategoryRequest;
import com.cevicheria.platform.dto.SupplierCategoryResponse;
import com.cevicheria.platform.service.SupplierCategoryService;
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
@RequestMapping("/supplier-categories")
public class SupplierCategoryController {
    private final SupplierCategoryService service;

    @GetMapping
    public List<SupplierCategoryResponse> getAll(@RequestParam Long businessId) {
        return service.getAll(businessId);
    }

    @GetMapping("/{id}")
    public SupplierCategoryResponse getById(@RequestParam Long businessId, @PathVariable Long id) {
        return service.getById(businessId, id);
    }

    @PostMapping
    public ResponseEntity<SupplierCategoryResponse> create(@RequestBody SupplierCategoryRequest request) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    @PatchMapping("/{id}")
    public SupplierCategoryResponse update(@RequestParam Long businessId, @PathVariable Long id,
                              @RequestBody SupplierCategoryRequest request) {
        return service.update(businessId, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestParam Long businessId, @PathVariable Long id) {
        service.delete(businessId, id);
        return ResponseEntity.noContent().build();
    }
}
