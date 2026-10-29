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

import com.cevicheria.platform.model.RecipeIngredient;
import com.cevicheria.platform.service.RecipeIngredientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/recipe-ingredients")
@RequiredArgsConstructor
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;

    @GetMapping
    public List<RecipeIngredient> getAll(@RequestParam Long businessId) {
        return recipeIngredientService.getAllByBusiness(businessId);
    }

    @GetMapping("/{id}")
    public RecipeIngredient getById(@PathVariable Long id) {
        return recipeIngredientService.getById(id);
    }

    @GetMapping("/recipe/{recipeId}")
    public List<RecipeIngredient> getByRecipe(@RequestParam Long businessId, @PathVariable Long recipeId) {
        return recipeIngredientService.getByRecipe(businessId, recipeId);
    }

    @GetMapping("/ingredient/{ingredientId}")
    public List<RecipeIngredient> getByIngredient(@RequestParam Long businessId, @PathVariable Long ingredientId) {
        return recipeIngredientService.getByIngredient(businessId, ingredientId);
    }

    @PostMapping
    public ResponseEntity<RecipeIngredient> create(@RequestBody RecipeIngredient ingredient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeIngredientService.save(ingredient));
    }

    @PutMapping("/{id}")
    public RecipeIngredient update(@PathVariable Long id, @RequestBody RecipeIngredient ingredient) {
        return recipeIngredientService.update(id, ingredient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recipeIngredientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}