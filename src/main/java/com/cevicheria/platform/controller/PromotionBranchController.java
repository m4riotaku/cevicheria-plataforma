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

import com.cevicheria.platform.model.PromotionBranch;
import com.cevicheria.platform.model.id.PromotionBranchId;
import com.cevicheria.platform.service.PromotionBranchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/promotions/{promotionId}/branches")
@RequiredArgsConstructor
public class PromotionBranchController {

    private final PromotionBranchService promotionBranchService;

    @GetMapping
    public List<PromotionBranch> getByPromotion(
            @RequestParam Long businessId, @PathVariable Long promotionId) {
        return promotionBranchService.getByPromotion(businessId, promotionId);
    }

    @PostMapping
    public ResponseEntity<PromotionBranch> create(@RequestBody PromotionBranch promotionBranch) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionBranchService.save(promotionBranch));
    }

    @DeleteMapping("/{branchId}")
    public ResponseEntity<Void> delete(
            @RequestParam Long businessId,
            @PathVariable Long promotionId,
            @PathVariable Long branchId) {
        promotionBranchService.deleteById(new PromotionBranchId(businessId, promotionId, branchId));
        return ResponseEntity.noContent().build();
    }
}