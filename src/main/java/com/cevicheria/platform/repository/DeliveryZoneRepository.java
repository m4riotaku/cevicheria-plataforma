package com.cevicheria.platform.repository;

import java.util.Optional;

import java.util.List;

import com.cevicheria.platform.model.DeliveryZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeliveryZoneRepository extends JpaRepository<DeliveryZone, Long> {
    Optional<DeliveryZone> findByBusinessIdAndName(Long businessId, String name);

    List<DeliveryZone> findByBusinessIdAndBranchIdAndActive(Long businessId, Long branchId, boolean active);

    Optional<DeliveryZone> findByBusinessIdAndBranchIdAndName(Long businessId, Long branchId, String name);
    Optional<DeliveryZone> findByIdAndBusinessId(Long id, Long businessId);
    List<DeliveryZone> findByBusinessIdAndBranchId(Long businessId, Long branchId);
}
