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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "dining_table_id")
    private DiningTable diningTable;

    private String contactName;

    private String phone;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    private Integer guestCount;

    private Integer toleranceMinutes = 15;

    private BigDecimal requiredDeposit = new BigDecimal("0.00");

    private String status = "PENDIENTE";

    private String notes;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public Reservation(Business business, Branch branch, String contactName, String phone, LocalDateTime startsAt, LocalDateTime endsAt, Integer guestCount) {
        changeBusiness(business);
        changeBranch(branch);
        changeContactName(contactName);
        changePhone(phone);
        changeStartsAt(startsAt);
        changeEndsAt(endsAt);
        changeGuestCount(guestCount);
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

    private void changeCustomer(Customer value) {
        this.customer = value;
    }

    public void changeDiningTable(DiningTable value) {
        this.diningTable = value;
    }

    public void changeContactName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El contact nombre es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El contact nombre no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 150) {
            throw new InvalidEntityDataException("El contact nombre no puede superar 150 caracteres");
        }
        this.contactName = normalized;
    }

    public void changePhone(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El teléfono es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El teléfono no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 30) {
            throw new InvalidEntityDataException("El teléfono no puede superar 30 caracteres");
        }
        this.phone = normalized;
    }

    public void changeStartsAt(LocalDateTime value) {
        if (value == null) {
            throw new InvalidEntityDataException("El fecha de inicio es obligatorio");
        }
        this.startsAt = value;
    }

    public void changeEndsAt(LocalDateTime value) {
        if (value == null) {
            throw new InvalidEntityDataException("El fecha de fin es obligatorio");
        }
        if (value != null && this.startsAt != null && !value.isAfter(this.startsAt)) {
            throw new InvalidEntityDataException("La fecha de fin debe ser posterior al inicio");
        }
        this.endsAt = value;
    }

    public void changeGuestCount(Integer value) {
        if (value == null) {
            throw new InvalidEntityDataException("El guest count es obligatorio");
        }
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El guest count no puede ser negativo");
        }
        if (value != null && value > 65535) {
            throw new InvalidEntityDataException("El guest count supera el máximo permitido");
        }
        if (value != null && value <= 0) {
            throw new InvalidEntityDataException("El guest count debe ser mayor que cero");
        }
        this.guestCount = value;
    }

    public void changeToleranceMinutes(Integer value) {
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El tolerance minutes no puede ser negativo");
        }
        if (value != null && value > 65535) {
            throw new InvalidEntityDataException("El tolerance minutes supera el máximo permitido");
        }
        this.toleranceMinutes = value;
    }

    public void changeRequiredDeposit(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El required deposit supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El required deposit no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El required deposit no puede ser negativo");
        }
        this.requiredDeposit = value;
    }

    public void changeStatus(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El estado es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El estado no puede estar vacío");
        }
        String normalized = value.trim().toUpperCase(java.util.Locale.ROOT);
        if (!(normalized.equals("PENDIENTE") || normalized.equals("CONFIRMADA") || normalized.equals("ATENDIDA") || normalized.equals("CANCELADA") || normalized.equals("NO_ASISTIO"))) {
            throw new InvalidEntityDataException("El estado no coincide con los valores permitidos por la base de datos");
        }
        this.status = normalized;
    }

    public void changeNotes(String value) {
        if (value == null || value.isBlank()) {
            this.notes = null;
            return;
        }
        String normalized = value.trim();
        this.notes = normalized;
    }


    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<ReservationCancellationTerms> cancellationTerms = new ArrayList<>();

    public List<ReservationCancellationTerms> getCancellationTerms() {
        return List.copyOf(this.cancellationTerms);
    }
}