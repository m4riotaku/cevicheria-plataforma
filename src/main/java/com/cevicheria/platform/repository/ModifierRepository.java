package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Modifier;

public interface ModifierRepository extends JpaRepository<Modifier, Long> {

	List<Modifier> findByBusiness_Id(Long businessId);

	List<Modifier> findByBusiness_IdAndActiveTrue(Long businessId);

	List<Modifier> findByBusiness_IdAndProduct_Id(Long businessId, Long productId);

	List<Modifier> findByBusiness_IdAndCategory_Id(Long businessId, Long categoryId);
}
