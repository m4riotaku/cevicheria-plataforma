package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.Product;

public interface ProductService {

    List<Product> getAllByBusiness(Long businessId);

    List<Product> getActiveByBusiness(Long businessId);

    Product getById(Long id);

    Product getByBusinessAndCode(Long businessId, String code);

    Product save(Product product);

    Product update(Long id, Product product);

    void deleteById(Long id);
}