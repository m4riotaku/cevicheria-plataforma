package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.InventoryBatch;

public interface InventoryBatchRepository extends JpaRepository<InventoryBatch, Long>
{
}