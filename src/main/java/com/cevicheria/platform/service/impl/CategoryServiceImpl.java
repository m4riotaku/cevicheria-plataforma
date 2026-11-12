package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Category;
import com.cevicheria.platform.repository.CategoryRepository;
import com.cevicheria.platform.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllByBusiness(Long businessId) {
        return categoryRepository.findByBusinessId(businessId);
    }

    @Override
    public List<Category> getActiveByBusiness(Long businessId) {
        return categoryRepository.findByBusinessIdAndActiveTrue(businessId);
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", id));
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category category) {
        Category current = getById(id);
        if (category.getName() != null) {
            current.changeName(category.getName());
        }
        if (category.getType() != null) {
            current.changeType(category.getType());
        }
        if (category.isActive()) {
            current.activate();
        } else {
            current.deactivate();
        }
        return categoryRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.delete(getById(id));
    }
}