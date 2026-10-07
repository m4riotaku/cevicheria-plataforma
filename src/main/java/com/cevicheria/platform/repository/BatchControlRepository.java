package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.BatchControl;

public interface BatchControlRepository extends JpaRepository<BatchControl, Long> {

    List<BatchControl> findByInventoryBatchIdOrderByCheckedAtDesc(Long inventoryBatchId);
}