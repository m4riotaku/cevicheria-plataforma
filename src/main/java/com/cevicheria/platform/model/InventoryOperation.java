package com.cevicheria.platform.model;

import java.time.LocalDateTime;
import java.util.Map;

import com.cevicheria.platform.model.enums.InventoryOperationStatus;
import com.cevicheria.platform.model.enums.InventoryOperationType;
import com.cevicheria.platform.model.enums.InventoryReturnResolution;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "inventory_operation")
public class InventoryOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Enumerated(EnumType.STRING)
    private InventoryOperationType type;

    private LocalDateTime recordDate;

    private String operationKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_operation_id")
    private InventoryOperation sourceOperation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private CustomerOrder order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsible_employee_id")
    private Employee responsibleEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorized_by_user_business_id")
    private UserBusiness authorizedByUserBusiness;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_branch_id")
    private Branch destinationBranch;

    private LocalDateTime cutoffAt;

    @Enumerated(EnumType.STRING)
    private InventoryOperationStatus status = InventoryOperationStatus.BORRADOR;

    @Enumerated(EnumType.STRING)
    private InventoryReturnResolution returnResolution;

    private String reason;

    private String evidenceUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> operationData;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}