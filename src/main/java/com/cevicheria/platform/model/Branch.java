package com.cevicheria.platform.model;

import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Branch
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", referencedColumnName = "id")
    private Business business;

    private String name;

    private boolean active = true;

    private String address;

    private String ubigeoCode;

    private String department;

    private String province;

    private String district;

    private String phone;

    private String timezone = "America/Lima";

    private boolean dineInEnabled = true;

    private boolean takeoutEnabled = true;

    private boolean deliveryEnabled = false;

    private boolean reservationsEnabled = false;

    private boolean publicMenuEnabled = false;

    private String menuSlug;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String,Object> schedule;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String,Object> settings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_employee_id", referencedColumnName = "id")
    private Employee responsibleEmployee;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
