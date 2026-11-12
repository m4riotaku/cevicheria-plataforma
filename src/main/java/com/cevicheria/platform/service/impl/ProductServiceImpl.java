package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.dto.ProductCreateRequest;
import com.cevicheria.platform.dto.ProductUpdateRequest;
import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Product;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.Category;
import com.cevicheria.platform.model.Ingredient;
import com.cevicheria.platform.repository.BusinessRepository;
import com.cevicheria.platform.repository.CategoryRepository;
import com.cevicheria.platform.repository.IngredientRepository;
import com.cevicheria.platform.repository.ProductRepository;
import com.cevicheria.platform.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BusinessRepository businessRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;

    @Override
    public List<Product> getAllByBusiness(Long businessId) {
        return productRepository.findByBusinessId(businessId);
    }

    @Override
    public List<Product> getActiveByBusiness(Long businessId) {
        return productRepository.findByBusinessIdAndActiveTrue(businessId);
    }

    @Override
    public List<Product> getByCategory(Long businessId, Long categoryId) {
        return productRepository.findByBusinessIdAndCategoryId(businessId, categoryId);
    }

    @Override
    public List<Product> getActiveByCategory(Long businessId, Long categoryId) {
        return productRepository.findByBusinessIdAndActiveTrueAndCategoryId(businessId, categoryId);
    }

    @Override
    public List<Product> getByType(Long businessId, com.cevicheria.platform.model.enums.ProductType type) {
        return productRepository.findByBusinessIdAndType(businessId, type);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id));
    }

    @Override
    public Product getByBusinessAndCode(Long businessId, String code) {
        return productRepository.findByBusinessIdAndCode(businessId, code)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "businessId/code", businessId + "/" + code));
    }

    @Override
    public Product create(ProductCreateRequest request) {
        Business business = getBusiness(request.businessId());
        if (productRepository.existsByBusinessIdAndCode(request.businessId(), request.code())) {
            throw new com.cevicheria.platform.handlerException.ResourceConflictException(
                    "Ya existe un producto con el código indicado en el negocio");
        }

        Category category = getCategory(request.categoryId(), business);
        Ingredient directIngredient = getIngredient(request.directIngredientId(), business);
        Product product = new Product(business, request.name(), request.code(), request.type(), directIngredient);
        product.changeCategory(category);
        if (request.saleUnit() != null) {
            product.changeSaleUnit(request.saleUnit());
        }
        product.changeImageUrl(request.imageUrl());
        product.changeTaxCode(request.taxCode());
        product.changeDescription(request.description());
        return productRepository.save(product);
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
        if (product.getImageUrl() != null) {
            current.changeImageUrl(product.getImageUrl());
        }
        if (product.getTaxCode() != null) {
            current.changeTaxCode(product.getTaxCode());
        }
        if (product.getDescription() != null) {
            current.changeDescription(product.getDescription());
        }
        if (product.isActive()) {
            current.activate();
        } else {
            current.deactivate();
        }
        return productRepository.save(current);
    }

    @Override
    public Product update(Long id, ProductUpdateRequest request) {
        Product current = getById(id);
        if (request.code() != null
                && productRepository.existsByBusinessIdAndCodeAndIdNot(
                        current.getBusiness().getId(), request.code(), id)) {
            throw new com.cevicheria.platform.handlerException.ResourceConflictException(
                    "Ya existe un producto con el código indicado en el negocio");
        }
        if (request.name() != null) {
            current.changeName(request.name());
        }
        if (request.code() != null) {
            current.changeCode(request.code());
        }
        if (request.categoryId() != null) {
            current.changeCategory(getCategory(request.categoryId(), current.getBusiness()));
        }
        if (request.directIngredientId() != null) {
            current.changeDirectIngredient(getIngredient(request.directIngredientId(), current.getBusiness()));
        }
        if (request.saleUnit() != null) {
            current.changeSaleUnit(request.saleUnit());
        }
        if (request.imageUrl() != null) {
            current.changeImageUrl(request.imageUrl());
        }
        if (request.taxCode() != null) {
            current.changeTaxCode(request.taxCode());
        }
        if (request.description() != null) {
            current.changeDescription(request.description());
        }
        if (request.active() != null) {
            if (request.active()) {
                current.activate();
            } else {
                current.deactivate();
            }
        }
        return productRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.delete(getById(id));
    }

    private Business getBusiness(Long businessId) {
        if (businessId == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        return businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business", "ID", businessId));
    }

    private Category getCategory(Long categoryId, Business business) {
        if (categoryId == null) {
            return null;
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));
        validateSameBusiness(category.getBusiness(), business, "La categoría no pertenece al negocio indicado");
        return category;
    }

    private Ingredient getIngredient(Long ingredientId, Business business) {
        if (ingredientId == null) {
            return null;
        }
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "ID", ingredientId));
        validateSameBusiness(ingredient.getBusiness(), business, "El insumo no pertenece al negocio indicado");
        return ingredient;
    }

    private void validateSameBusiness(Business relatedBusiness, Business business, String message) {
        if (relatedBusiness == null || !business.getId().equals(relatedBusiness.getId())) {
            throw new InvalidEntityDataException(message);
        }
    }
}