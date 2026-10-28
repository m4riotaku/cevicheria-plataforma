package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.Product;
import com.cevicheria.platform.model.enums.ProductType;

public interface ProductService {

    List<Product> getAllByBusiness(Long businessId);

    List<Product> getActiveByBusiness(Long businessId);

    List<Product> getByCategory(Long businessId, Long categoryId);

    List<Product> getActiveByCategory(Long businessId, Long categoryId);

    List<Product> getByType(Long businessId, ProductType type);

    Product getById(Long id);

    Product getByBusinessAndCode(Long businessId, String code);

    Product save(Product product);

    Product update(Long id, Product product);

    void deleteById(Long id);
}