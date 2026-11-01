package com.cevicheria.platform.repository;

import java.util.Optional;

import java.util.List;

import com.cevicheria.platform.model.enums.ReservationStatus;

import com.cevicheria.platform.model.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>
{
    List<Reservation> findByBusinessIdAndBranchIdAndStatus(Long businessId, Long branchId, ReservationStatus status);
    //reservas en un rango de fechas (ej: vista del día)
    //List<Reservation> findByBusinessIdAndBranchIdAndScheduledAtBetween(Long businessId, Long branchId, LocalDateTime from, LocalDateTime to);
    List<Reservation> findByCustomerId(Long customerId);
    Optional<Reservation> findByIdAndBusinessId(Long id, Long businessId);
    List<Reservation> findByBusinessIdAndBranchId(Long businessId, Long branchId);
}
