package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.KdsSortOrder;
import com.cevicheria.platform.model.id.PreparationAreaKdsSettingId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreparationAreaKdsSetting {

    @EmbeddedId
    private PreparationAreaKdsSettingId id;

    @Enumerated(EnumType.STRING)
    private KdsSortOrder sortOrder = KdsSortOrder.CONFIRMACION;

    private Integer alertMinutes = 15;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("branchId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @MapsId("preparationAreaId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "preparation_area_id")
    private PreparationArea preparationArea;

    public PreparationAreaKdsSetting(
            Business business,
            Branch branch,
            PreparationArea preparationArea,
            KdsSortOrder sortOrder,
            Integer alertMinutes) {
        this.id = new PreparationAreaKdsSettingId(null, null, null);
        changeBusiness(business);
        changeBranch(branch);
        changePreparationArea(preparationArea);
        changeSortOrder(sortOrder);
        changeAlertMinutes(alertMinutes);
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
        this.id = new PreparationAreaKdsSettingId(business.getId(), this.id.getBranchId(), this.id.getPreparationAreaId());
    }

    private void changeBranch(Branch branch) {
        if (branch == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        if (branch.getId() == null) {
            throw new InvalidEntityDataException("El local debe estar registrado antes de realizar la asociación");
        }
        this.branch = branch;
        this.id = new PreparationAreaKdsSettingId(this.id.getBusinessId(), branch.getId(), this.id.getPreparationAreaId());
    }

    private void changePreparationArea(PreparationArea preparationArea) {
        if (preparationArea == null) {
            throw new InvalidEntityDataException("El área de preparación es obligatorio");
        }
        if (preparationArea.getId() == null) {
            throw new InvalidEntityDataException("El área de preparación debe estar registrado antes de realizar la asociación");
        }
        this.preparationArea = preparationArea;
        this.id = new PreparationAreaKdsSettingId(this.id.getBusinessId(), this.id.getBranchId(), preparationArea.getId());
    }

    public void changeSortOrder(KdsSortOrder sortOrder) {
        if (sortOrder == null) {
            throw new InvalidEntityDataException("El criterio de ordenación es obligatorio");
        }
        this.sortOrder = sortOrder;
    }

    public void changeAlertMinutes(Integer alertMinutes) {
        if (alertMinutes == null) {
            throw new InvalidEntityDataException("Los minutos de alerta son obligatorios");
        }
        if (alertMinutes < 1) {
            throw new InvalidEntityDataException("Los minutos de alerta debe ser mayor que cero");
        }
        if (alertMinutes > 65535) {
            throw new InvalidEntityDataException("Los minutos de alerta supera el máximo permitido");
        }
        this.alertMinutes = alertMinutes;
    }
}
