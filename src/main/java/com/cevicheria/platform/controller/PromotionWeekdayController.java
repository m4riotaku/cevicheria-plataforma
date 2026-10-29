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

import com.cevicheria.platform.model.PromotionWeekday;
import com.cevicheria.platform.model.id.PromotionWeekdayId;
import com.cevicheria.platform.service.PromotionWeekdayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/promotions/{promotionId}/weekdays")
@RequiredArgsConstructor
public class PromotionWeekdayController {

    private final PromotionWeekdayService promotionWeekdayService;

    @GetMapping
    public List<PromotionWeekday> getByPromotion(
            @RequestParam Long businessId, @PathVariable Long promotionId) {
        return promotionWeekdayService.getByPromotion(businessId, promotionId);
    }

    @PostMapping
    public ResponseEntity<PromotionWeekday> create(@RequestBody PromotionWeekday promotionWeekday) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionWeekdayService.save(promotionWeekday));
    }

    @DeleteMapping("/{weekday}")
    public ResponseEntity<Void> delete(
            @RequestParam Long businessId,
            @PathVariable Long promotionId,
            @PathVariable Integer weekday) {
        promotionWeekdayService.deleteById(new PromotionWeekdayId(businessId, promotionId, weekday));
        return ResponseEntity.noContent().build();
    }
}