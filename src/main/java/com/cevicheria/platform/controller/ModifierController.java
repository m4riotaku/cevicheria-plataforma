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

import com.cevicheria.platform.model.Modifier;
import com.cevicheria.platform.service.ModifierService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/modifiers")
@RequiredArgsConstructor
public class ModifierController {

    private final ModifierService modifierService;

    @GetMapping
    public List<Modifier> getAll(@RequestParam Long businessId) {
        return modifierService.getAllByBusiness(businessId);
    }

    @GetMapping("/active")
    public List<Modifier> getActive(@RequestParam Long businessId) {
        return modifierService.getActiveByBusiness(businessId);
    }

    @GetMapping("/{id}")
    public Modifier getById(@PathVariable Long id) {
        return modifierService.getById(id);
    }

    @GetMapping("/product/{productId}")
    public List<Modifier> getByProduct(@RequestParam Long businessId, @PathVariable Long productId) {
        return modifierService.getByProduct(businessId, productId);
    }

    @GetMapping("/product/{productId}/active")
    public List<Modifier> getActiveByProduct(@RequestParam Long businessId, @PathVariable Long productId) {
        return modifierService.getActiveByProduct(businessId, productId);
    }

    @GetMapping("/category/{categoryId}")
    public List<Modifier> getByCategory(@RequestParam Long businessId, @PathVariable Long categoryId) {
        return modifierService.getByCategory(businessId, categoryId);
    }

    @GetMapping("/category/{categoryId}/active")
    public List<Modifier> getActiveByCategory(@RequestParam Long businessId, @PathVariable Long categoryId) {
        return modifierService.getActiveByCategory(businessId, categoryId);
    }

    @PostMapping
    public ResponseEntity<Modifier> create(@RequestBody Modifier modifier) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modifierService.save(modifier));
    }

    @PutMapping("/{id}")
    public Modifier update(@PathVariable Long id, @RequestBody Modifier modifier) {
        return modifierService.update(id, modifier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        modifierService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}