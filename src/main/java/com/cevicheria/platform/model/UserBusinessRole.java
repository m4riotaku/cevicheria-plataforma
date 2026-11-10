package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/** Rol de alcance negocio; los roles de alcance sucursal siguen en UserRole. */
@Entity
@Getter
@Table(name = "user_business_role")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBusinessRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_business_id")
    private UserBusiness userBusiness;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id")
    private SecurityRole role;

    private boolean active = true;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public UserBusinessRole(Business business, UserBusiness userBusiness, SecurityRole role) {
        if (business == null || userBusiness == null || role == null
                || !business.getId().equals(userBusiness.getBusiness().getId())
                || !business.getId().equals(role.getBusiness().getId())) {
            throw new InvalidEntityDataException("El vínculo y el rol deben pertenecer al mismo negocio");
        }
        this.business = business;
        this.userBusiness = userBusiness;
        this.role = role;
    }

    public void activate() { this.active = true; }
    public void deactivate() { this.active = false; }
}
