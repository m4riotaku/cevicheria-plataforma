package com.cevicheria.platform.repository;
import com.cevicheria.platform.model.SupplierCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SupplierCategoryRepository extends JpaRepository<SupplierCategory, Long> {
    Optional<SupplierCategory> findByBusinessIdAndName(Long businessId, String name);
    List<SupplierCategory> findByBusinessId(Long businessId);
    Optional<SupplierCategory> findByIdAndBusinessId(Long id, Long businessId);
}
