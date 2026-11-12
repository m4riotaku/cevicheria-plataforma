package com.cevicheria.platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cevicheria.platform.dto.ProductCreateRequest;
import com.cevicheria.platform.dto.ProductResponse;
import com.cevicheria.platform.dto.ProductUpdateRequest;
import com.cevicheria.platform.model.Product;
import com.cevicheria.platform.model.enums.ProductType;
import com.cevicheria.platform.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getAllByBusiness(@RequestParam Long businessId) {
        return productService.getAllByBusiness(businessId).stream()
                .map(ProductController::toResponse)
                .toList();
    }

    @GetMapping("/active")
    public List<ProductResponse> getActiveByBusiness(@RequestParam Long businessId) {
        return productService.getActiveByBusiness(businessId).stream()
                .map(ProductController::toResponse)
                .toList();
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductResponse> getByCategory(
            @RequestParam Long businessId,
            @PathVariable Long categoryId) {
        return productService.getByCategory(businessId, categoryId).stream()
                .map(ProductController::toResponse)
                .toList();
    }

    @GetMapping("/category/{categoryId}/active")
    public List<ProductResponse> getActiveByCategory(
            @RequestParam Long businessId,
            @PathVariable Long categoryId) {
        return productService.getActiveByCategory(businessId, categoryId).stream()
                .map(ProductController::toResponse)
                .toList();
    }

    @GetMapping("/type/{type}")
    public List<ProductResponse> getByType(
            @RequestParam Long businessId,
            @PathVariable ProductType type) {
        return productService.getByType(businessId, type).stream()
                .map(ProductController::toResponse)
                .toList();
    }

    @GetMapping("/business/{businessId}/code/{code}")
    public ProductResponse getByBusinessAndCode(
            @PathVariable Long businessId,
            @PathVariable String code) {
        return toResponse(productService.getByBusinessAndCode(businessId, code));
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return toResponse(productService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(productService.create(request)));
    }

    @PatchMapping("/{id}")
    public ProductResponse update(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequest request) {
        return toResponse(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getBusiness().getId(),
                product.getName(),
                product.isActive(),
                product.getCategory() == null ? null : product.getCategory().getId(),
                product.getDirectIngredient() == null ? null : product.getDirectIngredient().getId(),
                product.getCode(),
                product.getType(),
                product.getSaleUnit(),
                product.getImageUrl(),
                product.getTaxCode(),
                product.getDescription(),
                product.getCreatedAt());
    }
}