package com.cevicheria.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	List<Recipe> findByBusiness_Id(Long businessId);

	List<Recipe> findByBusiness_IdAndProduct_Id(Long businessId, Long productId);

	Optional<Recipe> findByBusiness_IdAndProduct_IdAndVersion(Long businessId, Long productId, Integer version);

	boolean existsByBusiness_IdAndProduct_IdAndVersion(Long businessId, Long productId, Integer version);
}
