package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.Warehouse;

public interface WarehouseService {

    List<Warehouse> getAllByBusiness(Long businessId);

    List<Warehouse> getActiveByBusiness(Long businessId);

    List<Warehouse> getByBranch(Long businessId, Long branchId);

    List<Warehouse> getActiveByBranch(Long branchId);

    Warehouse getById(Long id);

    Warehouse save(Warehouse warehouse);

    Warehouse update(Long id, Warehouse warehouse);

    void deleteById(Long id);
}