package com.cevicheria.platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cevicheria.platform.model.PromotionProduct;
import com.cevicheria.platform.model.id.PromotionProductId;
import com.cevicheria.platform.service.PromotionProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/promotions/{promotionId}/products")
@RequiredArgsConstructor
public class PromotionProductController {

    private final PromotionProductService promotionProductService;

    @GetMapping
    public List<PromotionProduct> getByPromotion(
            @RequestParam Long businessId, @PathVariable Long promotionId) {
        return promotionProductService.getByPromotion(businessId, promotionId);
    }

    @PostMapping
    public ResponseEntity<PromotionProduct> create(@RequestBody PromotionProduct promotionProduct) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionProductService.save(promotionProduct));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(
            @RequestParam Long businessId,
            @PathVariable Long promotionId,
            @PathVariable Long productId) {
        promotionProductService.deleteById(new PromotionProductId(businessId, promotionId, productId));
        return ResponseEntity.noContent().build();
    }
}