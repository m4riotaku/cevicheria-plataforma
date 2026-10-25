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
public class UserBusiness {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private boolean owner = false;

    private boolean active = true;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public UserBusiness(Business business, UserAccount userAccount) {
        changeBusiness(business);
        changeUserAccount(userAccount);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    private void changeUserAccount(UserAccount value) {
        if (value == null) {
            throw new InvalidEntityDataException("El usuario es obligatorio");
        }
        this.userAccount = value;
    }

    private void changeEmployee(Employee value) {
        this.employee = value;
    }

    public void enableOwner() {
        this.owner = true;
    }

    public void disableOwner() {
        this.owner = false;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}