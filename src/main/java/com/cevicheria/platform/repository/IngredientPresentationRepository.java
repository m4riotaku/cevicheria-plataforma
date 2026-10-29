package com.cevicheria.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.IngredientPresentation;

public interface IngredientPresentationRepository extends JpaRepository<IngredientPresentation, Long> {

    List<IngredientPresentation> findByBusinessId(Long businessId);

    List<IngredientPresentation> findByBusinessIdAndIngredientId(Long businessId, Long ingredientId);

    List<IngredientPresentation> findByIngredientIdAndActiveTrue(Long ingredientId);

    List<IngredientPresentation> findByBusinessIdAndIngredientIdAndActiveTrue(Long businessId, Long ingredientId);
}