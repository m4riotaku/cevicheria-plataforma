package com.cevicheria.platform.controller;

import java.util.Optional;

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

import com.cevicheria.platform.model.PromotionRequirement;
import com.cevicheria.platform.model.id.PromotionRequirementId;
import com.cevicheria.platform.service.PromotionRequirementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/promotions/{promotionId}/requirements")
@RequiredArgsConstructor
public class PromotionRequirementController {

    private final PromotionRequirementService promotionRequirementService;

    @GetMapping
    public ResponseEntity<PromotionRequirement> getByPromotion(
            @RequestParam Long businessId, @PathVariable Long promotionId) {
        Optional<PromotionRequirement> requirement = promotionRequirementService
                .getByPromotion(businessId, promotionId);
        return ResponseEntity.of(requirement);
    }

    @PostMapping
    public ResponseEntity<PromotionRequirement> create(@RequestBody PromotionRequirement requirement) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionRequirementService.save(requirement));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestParam Long businessId, @PathVariable Long promotionId) {
        promotionRequirementService.deleteById(new PromotionRequirementId(businessId, promotionId));
        return ResponseEntity.noContent().build();
    }
}