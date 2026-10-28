package com.cevicheria.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	List<Recipe> findByBusinessId(Long businessId);

	List<Recipe> findByBusinessIdAndProductId(Long businessId, Long productId);

	List<Recipe> findByBusinessIdAndProductIdAndActiveTrue(Long businessId, Long productId);

	java.util.Optional<Recipe> findFirstByBusinessIdAndProductIdAndActiveTrueOrderByVersionDesc(
			Long businessId, Long productId);

	Optional<Recipe> findByBusinessIdAndProductIdAndVersion(Long businessId, Long productId, Integer version);

	boolean existsByBusinessIdAndProductIdAndVersion(Long businessId, Long productId, Integer version);
}
