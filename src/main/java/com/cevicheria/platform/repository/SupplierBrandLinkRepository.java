package com.cevicheria.platform.repository;
import com.cevicheria.platform.model.SupplierBrandLink;
import com.cevicheria.platform.model.id.SupplierBrandLinkId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierBrandLinkRepository extends JpaRepository<SupplierBrandLink, SupplierBrandLinkId> {
    List<SupplierBrandLink> findByBusinessIdAndSupplierId(Long businessId, Long supplierId);
}
