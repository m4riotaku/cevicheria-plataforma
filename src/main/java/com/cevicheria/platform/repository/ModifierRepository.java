package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Modifier;

public interface ModifierRepository extends JpaRepository<Modifier, Long> {

	List<Modifier> findByBusinessId(Long businessId);

	List<Modifier> findByBusinessIdAndActiveTrue(Long businessId);

	List<Modifier> findByBusinessIdAndProductId(Long businessId, Long productId);

	List<Modifier> findByBusinessIdAndProductIdAndActiveTrue(Long businessId, Long productId);

	List<Modifier> findByBusinessIdAndCategoryId(Long businessId, Long categoryId);

	List<Modifier> findByBusinessIdAndCategoryIdAndActiveTrue(Long businessId, Long categoryId);
}
