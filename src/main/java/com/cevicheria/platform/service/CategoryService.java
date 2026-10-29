package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.Category;

public interface CategoryService {

    List<Category> getAllByBusiness(Long businessId);

    List<Category> getActiveByBusiness(Long businessId);

    Category getById(Long id);

    Category save(Category category);

    Category update(Long id, Category category);

    void deleteById(Long id);
}