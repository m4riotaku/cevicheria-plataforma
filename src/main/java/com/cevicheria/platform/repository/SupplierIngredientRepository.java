package com.cevicheria.platform.repository;

import java.util.Optional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.SupplierIngredient;


@Repository
public interface SupplierIngredientRepository extends JpaRepository<SupplierIngredient, Long> {

    List<SupplierIngredient> findBySupplierId(Long supplierId);

    List<SupplierIngredient> findByIngredientId(Long ingredientId);

    Optional<SupplierIngredient> findBySupplierIdAndIngredientId(Long supplierId, Long ingredientId);

    // proveedor preferido para un ingrediente dado
    Optional<SupplierIngredient> findByIngredientIdAndPreferredTrue(Long ingredientId);
    Optional<SupplierIngredient> findByIdAndBusinessId(Long id, Long businessId);
    List<SupplierIngredient> findByBusinessId(Long businessId);
}
