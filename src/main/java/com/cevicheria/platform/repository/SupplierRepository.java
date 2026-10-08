package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    List<Supplier> findByBusinessIdAndActive(Long businessId, boolean active);

    Optional<Supplier> findByBusinessIdAndDocumentNumber(Long businessId, String documentNumber);

    boolean existsByBusinessIdAndDocumentNumber(Long businessId, String documentNumber);
}
