package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
