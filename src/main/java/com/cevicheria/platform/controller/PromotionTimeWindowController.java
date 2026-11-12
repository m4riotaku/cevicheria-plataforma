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

import com.cevicheria.platform.model.PromotionTimeWindow;
import com.cevicheria.platform.model.id.PromotionTimeWindowId;
import com.cevicheria.platform.service.PromotionTimeWindowService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/promotions/{promotionId}/time-windows")
@RequiredArgsConstructor
public class PromotionTimeWindowController {

    private final PromotionTimeWindowService promotionTimeWindowService;

    @GetMapping
    public List<PromotionTimeWindow> getByPromotion(
            @RequestParam Long businessId, @PathVariable Long promotionId) {
        return promotionTimeWindowService.getByPromotion(businessId, promotionId);
    }

    @PostMapping
    public ResponseEntity<PromotionTimeWindow> create(@RequestBody PromotionTimeWindow timeWindow) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionTimeWindowService.save(timeWindow));
    }

    @DeleteMapping("/{sequenceNumber}")
    public ResponseEntity<Void> delete(
            @RequestParam Long businessId,
            @PathVariable Long promotionId,
            @PathVariable Integer sequenceNumber) {
        promotionTimeWindowService.deleteById(
                new PromotionTimeWindowId(businessId, promotionId, sequenceNumber));
        return ResponseEntity.noContent().build();
    }
}