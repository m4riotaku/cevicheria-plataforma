package com.cevicheria.platform.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.IngredientPresentation;

public interface IngredientPresentationRepository extends JpaRepository<IngredientPresentation, Long> {

    List<IngredientPresentation> findByIngredientIdAndActiveTrue(Long ingredientId);

    Optional<IngredientPresentation> findByIngredientIdAndDefaultPresentationTrue(Long ingredientId);
}