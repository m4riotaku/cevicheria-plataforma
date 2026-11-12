package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.ReservationCancellationTermsId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationCancellationTerms {

    @EmbeddedId
    private ReservationCancellationTermsId id;

    private Integer fullRefundBeforeHours;

    private BigDecimal noShowRetentionAmount;

    private String policyNotes;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("branchId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @MapsId("reservationId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public ReservationCancellationTerms(
            Business business,
            Branch branch,
            Reservation reservation,
            Integer fullRefundBeforeHours,
            BigDecimal noShowRetentionAmount,
            String policyNotes) {
        this.id = new ReservationCancellationTermsId(null, null, null);
        changeBusiness(business);
        changeBranch(branch);
        changeReservation(reservation);
        changeFullRefundBeforeHours(fullRefundBeforeHours);
        changeNoShowRetentionAmount(noShowRetentionAmount);
        changePolicyNotes(policyNotes);
        if (business.getId() != null && branch.getBusiness() != null
                && branch.getBusiness().getId() != null
                && !business.getId().equals(branch.getBusiness().getId())) {
            throw new InvalidEntityDataException("El local no pertenece al negocio seleccionado");
        }
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new ReservationCancellationTermsId(business.getId(), this.id.getBranchId(), this.id.getReservationId());
    }

    private void changeBranch(Branch branch) {
        if (branch == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        if (branch.getId() == null) {
            throw new InvalidEntityDataException("El local debe estar registrado antes de realizar la asociación");
        }
        this.branch = branch;
        this.id = new ReservationCancellationTermsId(this.id.getBusinessId(), branch.getId(), this.id.getReservationId());
    }

    private void changeReservation(Reservation reservation) {
        if (reservation == null) {
            throw new InvalidEntityDataException("La reserva es obligatoria");
        }
        if (reservation.getId() == null) {
            throw new InvalidEntityDataException("La reserva debe estar registrada antes de realizar la asociación");
        }
        this.reservation = reservation;
        this.id = new ReservationCancellationTermsId(this.id.getBusinessId(), this.id.getBranchId(), reservation.getId());
    }

    public void changeFullRefundBeforeHours(Integer fullRefundBeforeHours) {
        if (fullRefundBeforeHours == null) {
            this.fullRefundBeforeHours = null;
            return;
        }
        if (fullRefundBeforeHours < 0) {
            throw new InvalidEntityDataException("El plazo de reembolso completo debe ser cero o positivo");
        }
        if (fullRefundBeforeHours > 65535) {
            throw new InvalidEntityDataException("El plazo de reembolso completo supera el máximo permitido");
        }
        this.fullRefundBeforeHours = fullRefundBeforeHours;
    }

    public void changeNoShowRetentionAmount(BigDecimal noShowRetentionAmount) {
        if (noShowRetentionAmount == null) {
            this.noShowRetentionAmount = null;
            return;
        }
        if (noShowRetentionAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidEntityDataException("El importe de retención por inasistencia debe ser cero o positivo");
        }
        if (noShowRetentionAmount.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El importe de retención por inasistencia excede DECIMAL(18,2)");
        }
        if (noShowRetentionAmount.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El importe de retención por inasistencia admite hasta 2 decimales");
        }
        this.noShowRetentionAmount = noShowRetentionAmount.setScale(2);
    }

    public void changePolicyNotes(String policyNotes) {
        if (policyNotes == null || policyNotes.isBlank()) {
            this.policyNotes = null;
            return;
        }
        String normalizedValue = policyNotes.trim();
        if (normalizedValue.length() > 500) {
            throw new InvalidEntityDataException("Las notas de la política no puede superar 500 caracteres");
        }
        this.policyNotes = normalizedValue;
    }
}
