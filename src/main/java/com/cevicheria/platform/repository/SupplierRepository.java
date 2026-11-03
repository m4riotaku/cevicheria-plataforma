package com.cevicheria.platform.repository;

import java.util.Optional;

import java.util.List;

import com.cevicheria.platform.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByBusinessIdAndDocumentTypeAndDocumentNumber(Long businessId, String documentType, String documentNumber);

    List<Supplier> findByBusinessIdAndActive(Long businessId, boolean active);

    Optional<Supplier> findByBusinessIdAndDocumentNumber(Long businessId, String documentNumber);

    boolean existsByBusinessIdAndDocumentNumber(Long businessId, String documentNumber);
    Optional<Supplier> findByIdAndBusinessId(Long id, Long businessId);
    List<Supplier> findByBusinessId(Long businessId);
}
