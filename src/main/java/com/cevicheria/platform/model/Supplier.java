package com.cevicheria.platform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;

    private String documentType;

    private String documentNumber;

    private String contactName;

    private String phone;

    private String email;

    private String address;

    private Integer creditDays = 0;

    private boolean active = true;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> categories;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> brands;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}
