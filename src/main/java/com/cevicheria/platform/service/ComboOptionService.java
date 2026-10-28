package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.ComboOption;

public interface ComboOptionService {

    List<ComboOption> getAllByBusiness(Long businessId);

    List<ComboOption> getByComboProduct(Long businessId, Long comboProductId);

    ComboOption getById(Long id);

    ComboOption save(ComboOption comboOption);

    ComboOption update(Long id, ComboOption comboOption);

    void deleteById(Long id);
}