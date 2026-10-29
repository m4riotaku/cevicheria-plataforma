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

import com.cevicheria.platform.model.ModifierIngredient;
import com.cevicheria.platform.service.ModifierIngredientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/modifier-ingredients")
@RequiredArgsConstructor
public class ModifierIngredientController {

    private final ModifierIngredientService modifierIngredientService;

    @GetMapping
    public List<ModifierIngredient> getAll(@RequestParam Long businessId) {
        return modifierIngredientService.getAllByBusiness(businessId);
    }

    @GetMapping("/{id}")
    public ModifierIngredient getById(@PathVariable Long id) {
        return modifierIngredientService.getById(id);
    }

    @GetMapping("/modifier/{modifierId}")
    public List<ModifierIngredient> getByModifier(@RequestParam Long businessId, @PathVariable Long modifierId) {
        return modifierIngredientService.getByModifier(businessId, modifierId);
    }

    @GetMapping("/ingredient/{ingredientId}")
    public List<ModifierIngredient> getByIngredient(@RequestParam Long businessId, @PathVariable Long ingredientId) {
        return modifierIngredientService.getByIngredient(businessId, ingredientId);
    }

    @PostMapping
    public ResponseEntity<ModifierIngredient> create(@RequestBody ModifierIngredient ingredient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modifierIngredientService.save(ingredient));
    }

    @PutMapping("/{id}")
    public ModifierIngredient update(@PathVariable Long id, @RequestBody ModifierIngredient ingredient) {
        return modifierIngredientService.update(id, ingredient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        modifierIngredientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}