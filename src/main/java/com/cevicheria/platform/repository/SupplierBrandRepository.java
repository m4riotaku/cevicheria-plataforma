package com.cevicheria.platform.repository;
import com.cevicheria.platform.model.SupplierBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SupplierBrandRepository extends JpaRepository<SupplierBrand, Long> {
    Optional<SupplierBrand> findByBusinessIdAndName(Long businessId, String name);
    List<SupplierBrand> findByBusinessId(Long businessId);
    Optional<SupplierBrand> findByIdAndBusinessId(Long id, Long businessId);
}
