package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.InventoryOperation;

public interface InventoryOperationRepository extends JpaRepository<InventoryOperation, Long> {

    List<InventoryOperation> findByBusinessIdOrderByCreatedAtDesc(Long businessId);
}