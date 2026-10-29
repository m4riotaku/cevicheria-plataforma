package com.cevicheria.platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cevicheria.platform.model.ComboOption;
import com.cevicheria.platform.service.ComboOptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/combo-options")
@RequiredArgsConstructor
public class ComboOptionController {

    private final ComboOptionService comboOptionService;

    @GetMapping
    public List<ComboOption> getAll(@RequestParam Long businessId) {
        return comboOptionService.getAllByBusiness(businessId);
    }

    @GetMapping("/{id}")
    public ComboOption getById(@PathVariable Long id) {
        return comboOptionService.getById(id);
    }

    @GetMapping("/combo/{comboProductId}")
    public List<ComboOption> getByComboProduct(@RequestParam Long businessId, @PathVariable Long comboProductId) {
        return comboOptionService.getByComboProduct(businessId, comboProductId);
    }

    @GetMapping("/combo/{comboProductId}/active")
    public List<ComboOption> getActiveByComboProduct(
            @RequestParam Long businessId, @PathVariable Long comboProductId) {
        return comboOptionService.getActiveByComboProduct(businessId, comboProductId);
    }

    @GetMapping("/option/{optionProductId}")
    public List<ComboOption> getByOptionProduct(@RequestParam Long businessId, @PathVariable Long optionProductId) {
        return comboOptionService.getByOptionProduct(businessId, optionProductId);
    }

    @PostMapping
    public ResponseEntity<ComboOption> create(@RequestBody ComboOption comboOption) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comboOptionService.save(comboOption));
    }

    @PutMapping("/{id}")
    public ComboOption update(@PathVariable Long id, @RequestBody ComboOption comboOption) {
        return comboOptionService.update(id, comboOption);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        comboOptionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}