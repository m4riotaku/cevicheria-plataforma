package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

}
