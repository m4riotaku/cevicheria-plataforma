package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.ModifierIngredient;

public interface ModifierIngredientRepository extends JpaRepository<ModifierIngredient, Long> {

	List<ModifierIngredient> findByBusiness_Id(Long businessId);

	List<ModifierIngredient> findByBusiness_IdAndModifier_Id(Long businessId, Long modifierId);

	List<ModifierIngredient> findByBusiness_IdAndIngredient_Id(Long businessId, Long ingredientId);
}
