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

import com.cevicheria.platform.model.Recipe;
import com.cevicheria.platform.service.RecipeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public List<Recipe> getAll(@RequestParam Long businessId) {
        return recipeService.getAllByBusiness(businessId);
    }

    @GetMapping("/{id}")
    public Recipe getById(@PathVariable Long id) {
        return recipeService.getById(id);
    }

    @GetMapping("/product/{productId}")
    public List<Recipe> getByProduct(@RequestParam Long businessId, @PathVariable Long productId) {
        return recipeService.getByProduct(businessId, productId);
    }

    @GetMapping("/product/{productId}/active")
    public List<Recipe> getActiveByProduct(@RequestParam Long businessId, @PathVariable Long productId) {
        return recipeService.getActiveByProduct(businessId, productId);
    }

    @GetMapping("/product/{productId}/latest")
    public Recipe getLatestActiveByProduct(@RequestParam Long businessId, @PathVariable Long productId) {
        return recipeService.getLatestActiveByProduct(businessId, productId);
    }

    @PostMapping
    public ResponseEntity<Recipe> create(@RequestBody Recipe recipe) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.save(recipe));
    }

    @PutMapping("/{id}")
    public Recipe update(@PathVariable Long id, @RequestBody Recipe recipe) {
        return recipeService.update(id, recipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recipeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}