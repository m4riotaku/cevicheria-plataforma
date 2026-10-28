package com.cevicheria.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Product;
import com.cevicheria.platform.model.enums.ProductType;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByBusinessId(Long businessId);

	List<Product> findByBusinessIdAndActiveTrue(Long businessId);

	List<Product> findByBusinessIdAndCategoryId(Long businessId, Long categoryId);

	List<Product> findByBusinessIdAndActiveTrueAndCategoryId(Long businessId, Long categoryId);

	List<Product> findByBusinessIdAndType(Long businessId, ProductType type);

	Optional<Product> findByBusinessIdAndCode(Long businessId, String code);

	boolean existsByBusinessIdAndCode(Long businessId, String code);
}
