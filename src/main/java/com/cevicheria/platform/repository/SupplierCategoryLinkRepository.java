package com.cevicheria.platform.repository;
import com.cevicheria.platform.model.SupplierCategoryLink;
import com.cevicheria.platform.model.id.SupplierCategoryLinkId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierCategoryLinkRepository extends JpaRepository<SupplierCategoryLink, SupplierCategoryLinkId> {
    List<SupplierCategoryLink> findByBusinessIdAndSupplierId(Long businessId, Long supplierId);
}
