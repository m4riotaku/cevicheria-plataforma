package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.BusinessTaxRegimenSource;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BusinessTaxRegimes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tax_regime_id", nullable = false)
    private TaxRegimes taxRegimes;

    @Enumerated(EnumType.STRING)
    private BusinessTaxRegimenSource source;

    private LocalDateTime verifiedAt;

    private LocalDate validFrom;

    private LocalDate validUntil;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public BusinessTaxRegimes(Business business, TaxRegimes taxRegimes,
                              String source, LocalDate validFrom) {
        this(business, taxRegimes, source, validFrom, null);
    }

    public BusinessTaxRegimes(Business business, TaxRegimes taxRegimes,
                              String source, LocalDate validFrom, LocalDate validUntil) {
        changeBusiness(business);
        changeTaxRegimes(taxRegimes);
        changeSource(source);
        changeValidFrom(validFrom);
        changeValidUntil(validUntil);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }

        this.business = business;
    }

    private void changeTaxRegimes(TaxRegimes taxRegimes) {
        if (taxRegimes == null) {
            throw new InvalidEntityDataException("El régimen tributario es obligatorio");
        }

        this.taxRegimes = taxRegimes;
    }

    private void changeSource(String source) {
        if (source == null || source.isBlank()) {
            throw new InvalidEntityDataException("La fuente del régimen tributario es obligatoria");
        }

        String normalizedSource = source.trim().toUpperCase(Locale.ROOT);

        if (!BusinessTaxRegimenSource.isValid(normalizedSource)) {
            throw new InvalidEntityDataException("La fuente del régimen tributario no es válida");
        }

        this.source = BusinessTaxRegimenSource.valueOf(normalizedSource);
    }

    private void changeValidFrom(LocalDate validFrom) {
        if (validFrom == null) {
            throw new InvalidEntityDataException("La fecha de inicio del régimen tributario es obligatoria");
        }

        this.validFrom = validFrom;
    }

    private void changeValidUntil(LocalDate validUntil) {
        if (validUntil != null && validUntil.isBefore(this.validFrom)) {
            throw new InvalidEntityDataException("La fecha de fin no puede ser anterior al inicio del régimen tributario");
        }

        this.validUntil = validUntil;
    }

    public void markVerified(LocalDateTime verifiedAt) {
        if (verifiedAt == null) {
            throw new InvalidEntityDataException("La fecha de verificación es obligatoria");
        }

        if (this.verifiedAt != null) {
            throw new InvalidEntityDataException("Este régimen tributario ya fue verificado");
        }

        this.verifiedAt = verifiedAt;
    }

    public void closeValidity(LocalDate validUntil) {
        if (validUntil == null) {
            throw new InvalidEntityDataException("La fecha de fin del régimen tributario es obligatoria");
        }

        if (this.validUntil != null) {
            throw new InvalidEntityDataException("La vigencia del régimen tributario ya fue cerrada");
        }

        changeValidUntil(validUntil);
    }
}
