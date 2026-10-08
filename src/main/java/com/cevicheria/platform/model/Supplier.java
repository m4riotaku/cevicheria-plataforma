package com.cevicheria.platform.model;

import com.cevicheria.platform.model.enums.ContactMethod;
import com.cevicheria.platform.model.enums.DocumentType;
import com.cevicheria.platform.model.enums.PaymentCondition;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "supplier", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"business_id", "document_number"})
})
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: FK real cuando M4riotaku publique Business
    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 10)
    private DocumentType documentType;

    @Column(name = "document_number", nullable = false, length = 11)
    private String documentNumber;

    @Column(name = "business_name", nullable = false)
    private String businessName; // razón social

    @Column(name = "trade_name")
    private String tradeName;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 60)
    private String department;

    @Column(length = 60)
    private String province;

    @Column(length = 60)
    private String district;

    @Column(name = "supplier_category")
    private String supplierCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alternative_supplier_id")
    private Supplier alternativeSupplier; // proveedor de respaldo

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_condition", nullable = false, length = 10)
    private PaymentCondition paymentCondition;

    @Column(name = "credit_days", nullable = false)
    private Integer creditDays = 0; // 0 si es contado

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_contact_method", length = 20)
    private ContactMethod preferredContactMethod;
}
