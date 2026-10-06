package com.cevicheria.platform.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    List<Warehouse> findByBusinessIdAndActiveTrue(Long businessId);

    List<Warehouse> findByBranchIdAndActiveTrue(Long branchId);
}