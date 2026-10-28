package com.cevicheria.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByBusiness_Id(Long businessId);

	List<Product> findByBusiness_IdAndActiveTrue(Long businessId);

	Optional<Product> findByBusiness_IdAndCode(Long businessId, String code);

	boolean existsByBusiness_IdAndCode(Long businessId, String code);
}
