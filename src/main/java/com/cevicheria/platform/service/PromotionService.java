package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.Promotion;

public interface PromotionService {

    List<Promotion> getAllByBusiness(Long businessId);

    List<Promotion> getActiveByBusiness(Long businessId);

    Promotion getById(Long id);

    Promotion save(Promotion promotion);

    Promotion update(Long id, Promotion promotion);

    void deleteById(Long id);
}