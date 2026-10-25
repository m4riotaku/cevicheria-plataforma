package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.InventoryMovementType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operation_id")
    private InventoryOperation operation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_id")
    private InventoryBatch batch;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "purchase_item_id")
    private PurchaseItem purchaseItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "order_consumption_id")
    private OrderConsumption orderConsumption;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "source_movement_id")
    private InventoryMovement sourceMovement;

    private Integer sequenceNumber;

    @Enumerated(EnumType.STRING)
    private InventoryMovementType type;

    private BigDecimal quantity = new BigDecimal("0");

    private BigDecimal unitCost = new BigDecimal("0");

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    private BigDecimal valueAmount;

    private BigDecimal expectedQuantity;

    private BigDecimal countedQuantity;

    private LocalDateTime recordDate;

    private String concept;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public InventoryMovement(Business business, Branch branch, InventoryOperation operation, InventoryBatch batch, Integer sequenceNumber, InventoryMovementType type, LocalDateTime recordDate, String concept) {
        changeBusiness(business);
        changeBranch(branch);
        changeOperation(operation);
        changeBatch(batch);
        changeSequenceNumber(sequenceNumber);
        changeType(type);
        changeRecordDate(recordDate);
        changeConcept(concept);
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

    private void changeOperation(InventoryOperation value) {
        if (value == null) {
            throw new InvalidEntityDataException("El operation es obligatorio");
        }
        this.operation = value;
    }

    private void changeBatch(InventoryBatch value) {
        if (value == null) {
            throw new InvalidEntityDataException("El lote es obligatorio");
        }
        this.batch = value;
    }

    private void changePurchaseItem(PurchaseItem value) {
        this.purchaseItem = value;
    }

    private void changeOrderConsumption(OrderConsumption value) {
        this.orderConsumption = value;
    }

    private void changeSourceMovement(InventoryMovement value) {
        this.sourceMovement = value;
    }

    public void changeSequenceNumber(Integer value) {
        if (value == null) {
            throw new InvalidEntityDataException("El sequence número es obligatorio");
        }
        if (value != null && value < 0) {
            throw new InvalidEntityDataException("El sequence número no puede ser negativo");
        }
        this.sequenceNumber = value;
    }

    private void changeType(InventoryMovementType value) {
        if (value == null) {
            throw new InvalidEntityDataException("El tipo es obligatorio");
        }
        this.type = value;
    }

    public void changeQuantity(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La cantidad supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La cantidad no admite más de 6 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("La cantidad no puede ser negativo");
        }
        if (value != null && value.signum() <= 0) {
            throw new InvalidEntityDataException("La cantidad debe ser mayor que cero");
        }
        this.quantity = value;
    }

    public void changeUnitCost(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("99999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("El unit cost supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("El unit cost no admite más de 6 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El unit cost no puede ser negativo");
        }
        this.unitCost = value;
    }

    public void changeExpectedQuantity(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La expected cantidad supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La expected cantidad no admite más de 6 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("La expected cantidad no puede ser negativo");
        }
        this.expectedQuantity = value;
    }

    public void changeCountedQuantity(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("999999999999.999999")) > 0) {
            throw new InvalidEntityDataException("La counted cantidad supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 6) {
            throw new InvalidEntityDataException("La counted cantidad no admite más de 6 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("La counted cantidad no puede ser negativo");
        }
        this.countedQuantity = value;
    }

    private void changeRecordDate(LocalDateTime value) {
        if (value == null) {
            throw new InvalidEntityDataException("La fecha de registro es obligatoria");
        }
        this.recordDate = value;
    }

    public void changeConcept(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El concept es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El concept no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 120) {
            throw new InvalidEntityDataException("El concept no puede superar 120 caracteres");
        }
        this.concept = normalized;
    }
}