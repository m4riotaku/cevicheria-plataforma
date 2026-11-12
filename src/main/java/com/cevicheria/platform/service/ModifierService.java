package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.Modifier;

public interface ModifierService {

    List<Modifier> getAllByBusiness(Long businessId);

    List<Modifier> getActiveByBusiness(Long businessId);

    List<Modifier> getByProduct(Long businessId, Long productId);

    List<Modifier> getActiveByProduct(Long businessId, Long productId);

    List<Modifier> getByCategory(Long businessId, Long categoryId);

    List<Modifier> getActiveByCategory(Long businessId, Long categoryId);

    Modifier getById(Long id);

    Modifier save(Modifier modifier);

    Modifier update(Long id, Modifier modifier);

    void deleteById(Long id);
}