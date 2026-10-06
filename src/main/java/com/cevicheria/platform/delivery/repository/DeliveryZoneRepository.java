package com.cevicheria.platform.delivery.repository;

import com.cevicheria.platform.delivery.model.DeliveryZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryZoneRepository extends JpaRepository<DeliveryZone, Long> {

    List<DeliveryZone> findByBusinessIdAndBranchIdAndActive(Long businessId, Long branchId, boolean active);

    Optional<DeliveryZone> findByBusinessIdAndBranchIdAndName(Long businessId, Long branchId, String name);
}
