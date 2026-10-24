package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.BranchOpeningHourId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BranchOpeningHour {

    @EmbeddedId
    private BranchOpeningHourId id;

    private LocalTime opensAt;

    private LocalTime closesAt;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("branchId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    public BranchOpeningHour(
            Business business,
            Branch branch,
            Integer weekday,
            Integer shiftNumber,
            LocalTime opensAt,
            LocalTime closesAt) {
        this.id = new BranchOpeningHourId(null, null, null, null);
        changeBusiness(business);
        changeBranch(branch);
        changeWeekday(weekday);
        changeShiftNumber(shiftNumber);
        changeOpensAt(opensAt);
        changeClosesAt(closesAt);
        if (business.getId() != null && branch.getBusiness() != null
                && branch.getBusiness().getId() != null
                && !business.getId().equals(branch.getBusiness().getId())) {
            throw new InvalidEntityDataException("El local no pertenece al negocio seleccionado");
        }
        validateTimeWindow(this.opensAt, this.closesAt);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new BranchOpeningHourId(business.getId(), this.id.getBranchId(), this.id.getWeekday(), this.id.getShiftNumber());
    }

    private void changeBranch(Branch branch) {
        if (branch == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        if (branch.getId() == null) {
            throw new InvalidEntityDataException("El local debe estar registrado antes de realizar la asociación");
        }
        this.branch = branch;
        this.id = new BranchOpeningHourId(this.id.getBusinessId(), branch.getId(), this.id.getWeekday(), this.id.getShiftNumber());
    }

    private void changeWeekday(Integer weekday) {
        if (weekday == null) {
            throw new InvalidEntityDataException("El día de la semana es obligatorio");
        }
        if (weekday < 1) {
            throw new InvalidEntityDataException("El dia de semana debe ser mayor o igual a 1");
        }
        if (weekday > 7) {
            throw new InvalidEntityDataException("El dia de semana debe ser menor o igual a 7");
        }
        this.id = new BranchOpeningHourId(this.id.getBusinessId(), this.id.getBranchId(), weekday, this.id.getShiftNumber());
    }

    private void changeShiftNumber(Integer shiftNumber) {
        if (shiftNumber == null) {
            throw new InvalidEntityDataException("El número de turno es obligatorio");
        }
        if (shiftNumber < 1) {
            throw new InvalidEntityDataException("El número de turno debe ser mayor que cero");
        }
        if (shiftNumber > 65535) {
            throw new InvalidEntityDataException("El número de turno supera el máximo permitido");
        }
        this.id = new BranchOpeningHourId(this.id.getBusinessId(), this.id.getBranchId(), this.id.getWeekday(), shiftNumber);
    }

    public void changeOpensAt(LocalTime opensAt) {
        if (opensAt == null) {
            throw new InvalidEntityDataException("La hora de apertura es obligatoria");
        }
        if (this.closesAt != null) {
            validateTimeWindow(opensAt, this.closesAt);
        }
        this.opensAt = opensAt;
    }

    public void changeClosesAt(LocalTime closesAt) {
        if (closesAt == null) {
            throw new InvalidEntityDataException("La hora de cierre es obligatoria");
        }
        if (this.opensAt != null) {
            validateTimeWindow(this.opensAt, closesAt);
        }
        this.closesAt = closesAt;
    }

    private static void validateTimeWindow(LocalTime start, LocalTime end) {
        if (start != null && end != null && !start.isBefore(end)) {
            throw new InvalidEntityDataException("El horario de inicio debe ser anterior al horario de fin");
        }
    }
}
