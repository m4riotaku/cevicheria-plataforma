package com.cevicheria.platform.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findByBusinessIdAndActiveTrue(Long businessId);

    List<Ingredient> findByBusinessIdAndCategoryIdAndActiveTrue(Long businessId, Long categoryId);

    Optional<Ingredient> findByBusinessIdAndCodeIgnoreCase(Long businessId, String code);

    boolean existsByBusinessIdAndCodeIgnoreCase(Long businessId, String code);
}