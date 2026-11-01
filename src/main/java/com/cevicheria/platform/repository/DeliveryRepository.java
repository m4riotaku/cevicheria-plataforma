package com.cevicheria.platform.repository;

import java.util.Optional;

import java.util.List;

import com.cevicheria.platform.model.enums.DeliveryStatus;

import com.cevicheria.platform.model.CustomerOrder;
import com.cevicheria.platform.model.Delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByBusinessIdAndBranchIdAndStatus(Long businessId, Long branchId, DeliveryStatus status);

    Optional<Delivery> findByOrder(CustomerOrder order);

    List<Delivery> findByDriverEmployeeIdAndStatus(Long driverEmployeeId, DeliveryStatus status);
    Optional<Delivery> findByIdAndBusinessId(Long id, Long businessId);
    List<Delivery> findByBusinessIdAndBranchId(Long businessId, Long branchId);
}
