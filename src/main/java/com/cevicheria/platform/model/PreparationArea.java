package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreparationArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private String name;

    private boolean active = true;

    private String printerDestination;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public PreparationArea(Business business, Branch branch, String name) {
        changeBusiness(business);
        changeBranch(branch);
        changeName(name);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    private void changeBranch(Branch value) {
        if (value == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        this.branch = value;
    }

    public void changeName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El nombre de la área de preparación es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El nombre del área de preparación no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 120) {
            throw new InvalidEntityDataException("El nombre del área de preparación no puede superar 120 caracteres");
        }
        this.name = normalized;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void changePrinterDestination(String value) {
        if (value == null || value.isBlank()) {
            this.printerDestination = null;
            return;
        }
        String normalized = value.trim();
        if (normalized.length() > 250) {
            throw new InvalidEntityDataException("El printer destination no puede superar 250 caracteres");
        }
        this.printerDestination = normalized;
    }


    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "preparationArea", fetch = FetchType.LAZY)
    private List<PreparationAreaKdsSetting> kdsSettings = new ArrayList<>();

    public List<PreparationAreaKdsSetting> getKdsSettings() {
        return List.copyOf(this.kdsSettings);
    }
}