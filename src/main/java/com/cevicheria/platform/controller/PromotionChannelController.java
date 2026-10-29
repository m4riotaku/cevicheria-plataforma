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

import com.cevicheria.platform.model.PromotionChannel;
import com.cevicheria.platform.model.enums.SalesChannel;
import com.cevicheria.platform.model.id.PromotionChannelId;
import com.cevicheria.platform.service.PromotionChannelService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/promotions/{promotionId}/channels")
@RequiredArgsConstructor
public class PromotionChannelController {

    private final PromotionChannelService promotionChannelService;

    @GetMapping
    public List<PromotionChannel> getByPromotion(
            @RequestParam Long businessId, @PathVariable Long promotionId) {
        return promotionChannelService.getByPromotion(businessId, promotionId);
    }

    @PostMapping
    public ResponseEntity<PromotionChannel> create(@RequestBody PromotionChannel promotionChannel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionChannelService.save(promotionChannel));
    }

    @DeleteMapping("/{salesChannel}")
    public ResponseEntity<Void> delete(
            @RequestParam Long businessId,
            @PathVariable Long promotionId,
            @PathVariable SalesChannel salesChannel) {
        promotionChannelService.deleteById(new PromotionChannelId(businessId, promotionId, salesChannel));
        return ResponseEntity.noContent().build();
    }
}