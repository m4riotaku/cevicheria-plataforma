package com.cevicheria.platform.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cevicheria.platform.model.IngredientPresentation;
import com.cevicheria.platform.service.IngredientPresentationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ingredient-presentations")
@RequiredArgsConstructor
public class IngredientPresentationController {
    private final IngredientPresentationService presentationService;

    @GetMapping
    public List<IngredientPresentation> getAll(@RequestParam Long businessId) { return presentationService.getAllByBusiness(businessId); }

    @GetMapping("/ingredient/{ingredientId}")
    public List<IngredientPresentation> getByIngredient(@RequestParam Long businessId, @PathVariable Long ingredientId) { return presentationService.getByIngredient(businessId, ingredientId); }

    @GetMapping("/ingredient/{ingredientId}/active")
    public List<IngredientPresentation> getActiveByIngredient(@RequestParam Long businessId, @PathVariable Long ingredientId) { return presentationService.getActiveByIngredient(businessId, ingredientId); }

    @GetMapping("/{id}")
    public IngredientPresentation getById(@PathVariable Long id) { return presentationService.getById(id); }

    @PostMapping
    public ResponseEntity<IngredientPresentation> create(@RequestBody IngredientPresentation presentation) { return ResponseEntity.status(HttpStatus.CREATED).body(presentationService.save(presentation)); }

    @PutMapping("/{id}")
    public IngredientPresentation update(@PathVariable Long id, @RequestBody IngredientPresentation presentation) { return presentationService.update(id, presentation); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { presentationService.deleteById(id); return ResponseEntity.noContent().build(); }
}