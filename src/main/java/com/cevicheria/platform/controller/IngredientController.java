package com.cevicheria.platform.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cevicheria.platform.model.Ingredient;
import com.cevicheria.platform.service.IngredientService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @GetMapping
    public List<Ingredient> getAll(@RequestParam Long businessId) { return ingredientService.getAllByBusiness(businessId); }

    @GetMapping("/active")
    public List<Ingredient> getActive(@RequestParam Long businessId) { return ingredientService.getActiveByBusiness(businessId); }

    @GetMapping("/category/{categoryId}/active")
    public List<Ingredient> getActiveByCategory(@RequestParam Long businessId, @PathVariable Long categoryId) {
        return ingredientService.getActiveByCategory(businessId, categoryId);
    }

    @GetMapping("/business/{businessId}/code/{code}")
    public Ingredient getByCode(@PathVariable Long businessId, @PathVariable String code) {
        return ingredientService.getByBusinessAndCode(businessId, code);
    }

    @GetMapping("/{id}")
    public Ingredient getById(@PathVariable Long id) { return ingredientService.getById(id); }

    @PostMapping
    public ResponseEntity<Ingredient> create(@RequestBody Ingredient ingredient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientService.save(ingredient));
    }

    @PutMapping("/{id}")
    public Ingredient update(@PathVariable Long id, @RequestBody Ingredient ingredient) { return ingredientService.update(id, ingredient); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { ingredientService.deleteById(id); return ResponseEntity.noContent().build(); }
}