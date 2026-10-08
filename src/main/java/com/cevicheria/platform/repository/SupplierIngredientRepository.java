package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.SupplierIngredient;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierIngredientRepository extends JpaRepository<SupplierIngredient, Long> {

    List<SupplierIngredient> findBySupplierId(Long supplierId);

    List<SupplierIngredient> findByIngredientId(Long ingredientId);

    Optional<SupplierIngredient> findBySupplierIdAndIngredientId(Long supplierId, Long ingredientId);

    // proveedor preferido para un ingrediente dado
    Optional<SupplierIngredient> findByIngredientIdAndPreferredTrue(Long ingredientId);
}
