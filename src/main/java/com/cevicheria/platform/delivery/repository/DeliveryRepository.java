package com.cevicheria.platform.delivery.repository;

import com.cevicheria.platform.delivery.model.Delivery;
import com.cevicheria.platform.delivery.model.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByBusinessIdAndBranchIdAndStatus(Long businessId, Long branchId, DeliveryStatus status);

    Optional<Delivery> findByCustomerOrderId(Long customerOrderId);

    // todos los pedidos asignados a un repartidor
    List<Delivery> findByDriverEmployeeIdAndStatus(Long driverEmployeeId, DeliveryStatus status);
}
