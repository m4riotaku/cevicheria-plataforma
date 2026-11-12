package com.cevicheria.platform.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cevicheria.platform.model.Category;
import com.cevicheria.platform.service.CategoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getAll(@RequestParam Long businessId) { return categoryService.getAllByBusiness(businessId); }

    @GetMapping("/active")
    public List<Category> getActive(@RequestParam Long businessId) { return categoryService.getActiveByBusiness(businessId); }

    @GetMapping("/{id}")
    public Category getById(@PathVariable Long id) { return categoryService.getById(id); }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category));
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @RequestBody Category category) { return categoryService.update(id, category); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { categoryService.deleteById(id); return ResponseEntity.noContent().build(); }
}