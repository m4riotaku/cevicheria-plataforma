package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.AccountRequest;
import com.cevicheria.platform.dto.AccountView;
import com.cevicheria.platform.dto.RegisterRequest;
import com.cevicheria.platform.dto.RegisterView;
import com.cevicheria.platform.dto.RequirementRequest;
import com.cevicheria.platform.dto.RequirementView;
import com.cevicheria.platform.dto.RuleRequest;
import com.cevicheria.platform.dto.RuleView;
import com.cevicheria.platform.dto.SeriesRequest;
import com.cevicheria.platform.dto.SeriesView;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.*;
import com.cevicheria.platform.model.id.CommissionRuleRequirementId;
import com.cevicheria.platform.service.BillingConfigurationService;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BillingConfigurationServiceImpl implements BillingConfigurationService {
    private final BillingSupport guard;
    private final EntityManager em;

    @Override
    public List<AccountView> accounts(Long b, Long br) {
        guard.write(b, br, "CONFIGURAR");
        return guard.byBranch(FinancialAccount.class, b, br).stream().map(this::accountView).toList();
    }
    @Override
    public AccountView account(Long b, Long br, Long id) {
        guard.write(b, br, "CONFIGURAR");
        return accountView(guard.local(FinancialAccount.class, id, b, br,
                FinancialAccount::getBusiness, FinancialAccount::getBranch));
    }
    @Override @Transactional
    public AccountView createAccount(AccountRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "CONFIGURAR");
        FinancialAccount value = new FinancialAccount(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), r.name(), r.type());
        applyAccount(value, r);
        em.persist(value);
        em.flush();
        return accountView(value);
    }
    @Override @Transactional
    public AccountView updateAccount(Long id, AccountRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "CONFIGURAR");
        FinancialAccount value = guard.local(FinancialAccount.class, id, r.businessId(), r.branchId(),
                FinancialAccount::getBusiness, FinancialAccount::getBranch);
        applyAccount(value, r);
        em.flush();
        return accountView(value);
    }
    private void applyAccount(FinancialAccount a, AccountRequest r) {
        if (r.name() != null) a.changeName(r.name());
        if (r.type() != null) a.changeType(r.type());
        if (r.currencyCode() != null) a.changeCurrencyCode(r.currencyCode());
        if (r.identifier() != null) a.changeIdentifier(r.identifier());
        if (r.openingBalance() != null) a.changeOpeningBalance(r.openingBalance());
        if (r.active() != null) { if (r.active()) a.activate(); else a.deactivate(); }
    }
    @Override @Transactional
    public void disableAccount(Long b, Long br, Long id) {
        guard.write(b, br, "CONFIGURAR");
        FinancialAccount value = guard.local(FinancialAccount.class, id, b, br,
                FinancialAccount::getBusiness, FinancialAccount::getBranch);
        value.deactivate();
    }
    private AccountView accountView(FinancialAccount a) {
        return new AccountView(a.getId(), a.getBusiness().getId(), a.getBranch().getId(),
                a.getName(), a.getType(), a.getCurrencyCode(), a.getIdentifier(),
                a.getOpeningBalance(), a.isActive());
    }

    @Override
    public List<RegisterView> registers(Long b, Long br) {
        guard.write(b, br, "CONFIGURAR");
        return guard.byBranch(CashRegister.class, b, br).stream().map(this::registerView).toList();
    }
    @Override
    public RegisterView register(Long b, Long br, Long id) {
        guard.write(b, br, "CONFIGURAR");
        return registerView(guard.local(CashRegister.class, id, b, br,
                CashRegister::getBusiness, CashRegister::getBranch));
    }
    @Override @Transactional
    public RegisterView createRegister(RegisterRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "CONFIGURAR");
        FinancialAccount account = guard.local(FinancialAccount.class, r.cashAccountId(), r.businessId(), r.branchId(),
                FinancialAccount::getBusiness, FinancialAccount::getBranch);
        guard.check(account.isActive() && account.getType().name().equals("EFECTIVO"),
                "La caja requiere una cuenta de efectivo activa");
        CashRegister value = new CashRegister(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), r.name(), account);
        if (Boolean.FALSE.equals(r.active())) value.deactivate();
        em.persist(value);
        em.flush();
        return registerView(value);
    }
    @Override @Transactional
    public RegisterView updateRegister(Long id, RegisterRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "CONFIGURAR");
        CashRegister value = guard.local(CashRegister.class, id, r.businessId(), r.branchId(),
                CashRegister::getBusiness, CashRegister::getBranch);
        if (r.cashAccountId() != null && !Objects.equals(r.cashAccountId(), value.getCashAccount().getId()))
            throw new BadRequestException("La cuenta de efectivo no se puede reemplazar; crea otra caja");
        if (r.name() != null) value.changeName(r.name());
        if (r.active() != null) { if (r.active()) value.activate(); else value.deactivate(); }
        em.flush();
        return registerView(value);
    }
    @Override @Transactional
    public void disableRegister(Long b, Long br, Long id) {
        guard.write(b, br, "CONFIGURAR");
        CashRegister value = guard.local(CashRegister.class, id, b, br,
                CashRegister::getBusiness, CashRegister::getBranch);
        value.deactivate();
    }
    private RegisterView registerView(CashRegister v) {
        return new RegisterView(v.getId(), v.getBusiness().getId(), v.getBranch().getId(),
                v.getName(), v.getCashAccount().getId(), v.isActive());
    }

    @Override
    public List<SeriesView> series(Long b, Long br) {
        guard.write(b, br, "CONFIGURAR");
        return guard.byBranch(ReceiptSeries.class, b, br).stream().map(this::seriesView).toList();
    }
    @Override
    public SeriesView singleSeries(Long b, Long br, Long id) {
        guard.write(b, br, "CONFIGURAR");
        return seriesView(guard.local(ReceiptSeries.class, id, b, br,
                ReceiptSeries::getBusiness, ReceiptSeries::getBranch));
    }
    @Override @Transactional
    public SeriesView createSeries(SeriesRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "CONFIGURAR");
        ReceiptSeries v = new ReceiptSeries(guard.business(r.businessId()),
                guard.branch(r.businessId(), r.branchId()), r.type(), r.series());
        if (Boolean.FALSE.equals(r.active())) v.deactivate();
        em.persist(v);
        em.flush();
        return seriesView(v);
    }
    @Override @Transactional
    public SeriesView updateSeries(Long id, SeriesRequest r) {
        guard.required(r, "request");
        guard.write(r.businessId(), r.branchId(), "CONFIGURAR");
        ReceiptSeries v = guard.local(ReceiptSeries.class, id, r.businessId(), r.branchId(),
                ReceiptSeries::getBusiness, ReceiptSeries::getBranch);
        if (r.type() != null && !r.type().equals(v.getType()))
            throw new BadRequestException("No se puede cambiar el tipo de una serie existente");
        if (r.series() != null && !r.series().equals(v.getSeries()))
            throw new BadRequestException("No se puede cambiar el código de una serie existente");
        if (r.active() != null) { if (r.active()) v.activate(); else v.deactivate(); }
        return seriesView(v);
    }
    @Override @Transactional
    public void disableSeries(Long b, Long br, Long id) {
        guard.write(b, br, "CONFIGURAR");
        ReceiptSeries v = guard.local(ReceiptSeries.class, id, b, br,
                ReceiptSeries::getBusiness, ReceiptSeries::getBranch);
        v.deactivate();
    }
    private SeriesView seriesView(ReceiptSeries s) {
        return new SeriesView(s.getId(), s.getBusiness().getId(), s.getBranch().getId(),
                s.getType(), s.getSeries(), s.getLastNumber(), s.isActive());
    }

    @Override
    public List<RuleView> rules(Long b) {
        guard.businessWrite(b);
        return guard.byBusiness(CommissionRule.class, b).stream().map(this::ruleView).toList();
    }
    @Override
    public RuleView rule(Long b, Long id) {
        guard.businessWrite(b);
        CommissionRule v = guard.require(CommissionRule.class, id);
        guard.businessScope(v.getBusiness(), b);
        return ruleView(v);
    }
    @Override @Transactional
    public RuleView createRule(RuleRequest r) {
        guard.required(r, "request");
        guard.businessWrite(r.businessId());
        CommissionRule v = new CommissionRule(guard.business(r.businessId()),
                r.name(), r.type(), r.validFrom());
        applyRule(v, r);
        em.persist(v);
        em.flush();
        return ruleView(v);
    }
    @Override @Transactional
    public RuleView updateRule(Long id, RuleRequest r) {
        guard.required(r, "request");
        guard.businessWrite(r.businessId());
        CommissionRule v = guard.require(CommissionRule.class, id);
        guard.businessScope(v.getBusiness(), r.businessId());
        if (r.type() != null) v.changeType(r.type());
        applyRule(v, r);
        return ruleView(v);
    }
    private void applyRule(CommissionRule v, RuleRequest r) {
        if (r.name() != null) v.changeName(r.name());
        if (r.valueAmount() != null) v.changeValueAmount(r.valueAmount());
        if (r.validUntil() != null) {
            guard.check(!r.validUntil().isBefore(v.getValidFrom()), "Fecha final anterior al inicio");
            v.changeValidUntil(r.validUntil());
        }
        if (r.active() != null) { if (r.active()) v.activate(); else v.deactivate(); }
    }
    @Override @Transactional
    public void disableRule(Long b, Long id) {
        guard.businessWrite(b);
        CommissionRule v = guard.require(CommissionRule.class, id);
        guard.businessScope(v.getBusiness(), b);
        v.deactivate();
    }
    private RuleView ruleView(CommissionRule v) {
        return new RuleView(v.getId(), v.getBusiness().getId(), v.getName(), v.getType(),
                v.getValueAmount(), v.getValidFrom(), v.getValidUntil(), v.isActive());
    }
    @Override
    public RequirementView requirement(Long b, Long ruleId) {
        guard.businessWrite(b);
        CommissionRuleRequirement v = em.find(CommissionRuleRequirement.class,
                new CommissionRuleRequirementId(b, ruleId));
        if (v == null) throw new ResourceNotFoundException("CommissionRuleRequirement", "ID", ruleId);
        return requirementView(v);
    }
    @Override @Transactional
    public RequirementView saveRequirement(Long ruleId, RequirementRequest r) {
        guard.required(r, "request");
        guard.check(ruleId != null && ruleId.equals(r.commissionRuleId()), "IDs no coinciden");
        guard.businessWrite(r.businessId());
        CommissionRule rule = guard.require(CommissionRule.class, r.commissionRuleId());
        guard.businessScope(rule.getBusiness(), r.businessId());
        CommissionRuleRequirement v = em.find(CommissionRuleRequirement.class,
                new CommissionRuleRequirementId(r.businessId(), r.commissionRuleId()));
        if (v == null) {
            v = new CommissionRuleRequirement(rule.getBusiness(), rule,
                    r.minimumSaleAmount(), r.salesChannel());
            em.persist(v);
        } else {
            v.changeMinimumSaleAmount(r.minimumSaleAmount());
            v.changeSalesChannel(r.salesChannel());
        }
        if (Boolean.TRUE.equals(r.onlyVerifiedPaidSales())) v.requireVerifiedPaidSales();
        else v.allowUnverifiedPaidSales();
        em.flush();
        return requirementView(v);
    }
    @Override @Transactional
    public void deleteRequirement(Long b, Long ruleId) {
        guard.businessWrite(b);
        CommissionRuleRequirement v = em.find(CommissionRuleRequirement.class,
                new CommissionRuleRequirementId(b, ruleId));
        if (v == null) throw new ResourceNotFoundException("CommissionRuleRequirement", "ID", ruleId);
        em.remove(v);
    }
    private RequirementView requirementView(CommissionRuleRequirement v) {
        return new RequirementView(v.getBusiness().getId(), v.getCommissionRule().getId(),
                v.isOnlyVerifiedPaidSales(), v.getMinimumSaleAmount(), v.getSalesChannel());
    }
}
