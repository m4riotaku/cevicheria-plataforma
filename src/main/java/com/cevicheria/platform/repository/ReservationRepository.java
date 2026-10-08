package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.Reservation;
import com.cevicheria.platform.model.enums.ReservationStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByBusinessIdAndBranchIdAndStatus(Long businessId, Long branchId, ReservationStatus status);

    // reservas en un rango de fechas (ej: vista del día)
    List<Reservation> findByBusinessIdAndBranchIdAndScheduledAtBetween(
            Long businessId, Long branchId, LocalDateTime from, LocalDateTime to);

    List<Reservation> findByCustomerId(Long customerId);
}
