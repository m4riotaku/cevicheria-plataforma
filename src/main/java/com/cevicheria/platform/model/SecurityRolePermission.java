package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.SecurityRolePermissionId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class SecurityRolePermission {

    @EmbeddedId
    private SecurityRolePermissionId id;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id")
    private SecurityRole role;

    @MapsId("permissionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id")
    private SecurityPermission permission;

    public SecurityRolePermission(
            Business business,
            SecurityRole role,
            SecurityPermission permission) {
        this.id = new SecurityRolePermissionId(null, null, null);
        changeBusiness(business);
        changeRole(role);
        changePermission(permission);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new SecurityRolePermissionId(business.getId(), this.id.getRoleId(), this.id.getPermissionId());
    }

    private void changeRole(SecurityRole role) {
        if (role == null) {
            throw new InvalidEntityDataException("El rol es obligatorio");
        }
        if (role.getId() == null) {
            throw new InvalidEntityDataException("El rol debe estar registrado antes de realizar la asociación");
        }
        this.role = role;
        this.id = new SecurityRolePermissionId(this.id.getBusinessId(), role.getId(), this.id.getPermissionId());
    }

    private void changePermission(SecurityPermission permission) {
        if (permission == null) {
            throw new InvalidEntityDataException("El permiso es obligatorio");
        }
        if (permission.getId() == null) {
            throw new InvalidEntityDataException("El permiso debe estar registrado antes de realizar la asociación");
        }
        this.permission = permission;
        this.id = new SecurityRolePermissionId(this.id.getBusinessId(), this.id.getRoleId(), permission.getId());
    }
}
