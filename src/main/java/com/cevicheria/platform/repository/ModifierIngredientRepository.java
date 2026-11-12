package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.ModifierIngredient;

public interface ModifierIngredientRepository extends JpaRepository<ModifierIngredient, Long> {

	List<ModifierIngredient> findByBusinessId(Long businessId);

	List<ModifierIngredient> findByBusinessIdAndModifierId(Long businessId, Long modifierId);

	List<ModifierIngredient> findByBusinessIdAndIngredientId(Long businessId, Long ingredientId);
}
