package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import jakarta.persistence.Entity;
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
public class UserRole {

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
    @JoinColumn(name = "user_business_id")
    private UserBusiness userBusiness;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id")
    private SecurityRole role;

    private boolean active = true;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public UserRole(Business business, Branch branch, UserBusiness userBusiness, SecurityRole role) {
        changeBusiness(business);
        changeBranch(branch);
        changeUserBusiness(userBusiness);
        changeRole(role);
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

    private void changeUserBusiness(UserBusiness value) {
        if (value == null) {
            throw new InvalidEntityDataException("El usuario negocio es obligatorio");
        }
        this.userBusiness = value;
    }

    private void changeRole(SecurityRole value) {
        if (value == null) {
            throw new InvalidEntityDataException("El rol es obligatorio");
        }
        this.role = value;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}