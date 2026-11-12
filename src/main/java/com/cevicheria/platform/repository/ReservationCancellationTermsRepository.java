package com.cevicheria.platform.repository;
import com.cevicheria.platform.model.ReservationCancellationTerms;
import com.cevicheria.platform.model.id.ReservationCancellationTermsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationCancellationTermsRepository
        extends JpaRepository<ReservationCancellationTerms, ReservationCancellationTermsId> {}
