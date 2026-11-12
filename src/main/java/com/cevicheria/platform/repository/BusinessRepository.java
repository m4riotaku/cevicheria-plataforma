package com.cevicheria.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Business;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    List<Business> findByActiveTrue();
    Optional<Business> findByName(String name);
    Optional<Business> findByRuc(String ruc);
}
