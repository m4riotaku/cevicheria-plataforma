package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByBusinessIdAndActiveTrue(Long businessId);
}