package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Product;
import com.cevicheria.platform.repository.ProductRepository;
import com.cevicheria.platform.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllByBusiness(Long businessId) {
        return productRepository.findByBusiness_Id(businessId);
    }

    @Override
    public List<Product> getActiveByBusiness(Long businessId) {
        return productRepository.findByBusiness_IdAndActiveTrue(businessId);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    @Override
    public Product getByBusinessAndCode(Long businessId, String code) {
        return productRepository.findByBusiness_IdAndCode(businessId, code)
                .orElseThrow(() -> new ResourceNotFoundException("Product", businessId));
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product current = getById(id);
        if (product.getName() != null) {
            current.changeName(product.getName());
        }
        if (product.getCode() != null) {
            current.changeCode(product.getCode());
        }
        if (product.getCategory() != null) {
            current.changeCategory(product.getCategory());
        }
        if (product.getDirectIngredient() != null) {
            current.changeDirectIngredient(product.getDirectIngredient());
        }
        if (product.getSaleUnit() != null) {
            current.changeSaleUnit(product.getSaleUnit());
        }
        current.changeImageUrl(product.getImageUrl());
        current.changeTaxCode(product.getTaxCode());
        current.changeDescription(product.getDescription());
        if (product.isActive()) {
            current.activate();
        } else {
            current.deactivate();
        }
        return productRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.delete(getById(id));
    }
}