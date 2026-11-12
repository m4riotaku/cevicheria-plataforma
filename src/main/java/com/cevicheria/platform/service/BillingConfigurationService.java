package com.cevicheria.platform.service;

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
import java.util.List;

public interface BillingConfigurationService {
    List<AccountView> accounts(Long businessId, Long branchId);
    AccountView account(Long businessId, Long branchId, Long id);
    AccountView createAccount(AccountRequest request);
    AccountView updateAccount(Long id, AccountRequest request);
    void disableAccount(Long businessId, Long branchId, Long id);

    List<RegisterView> registers(Long businessId, Long branchId);
    RegisterView register(Long businessId, Long branchId, Long id);
    RegisterView createRegister(RegisterRequest request);
    RegisterView updateRegister(Long id, RegisterRequest request);
    void disableRegister(Long businessId, Long branchId, Long id);

    List<SeriesView> series(Long businessId, Long branchId);
    SeriesView singleSeries(Long businessId, Long branchId, Long id);
    SeriesView createSeries(SeriesRequest request);
    SeriesView updateSeries(Long id, SeriesRequest request);
    void disableSeries(Long businessId, Long branchId, Long id);

    List<RuleView> rules(Long businessId);
    RuleView rule(Long businessId, Long id);
    RuleView createRule(RuleRequest request);
    RuleView updateRule(Long id, RuleRequest request);
    void disableRule(Long businessId, Long id);

    RequirementView requirement(Long businessId, Long ruleId);
    RequirementView saveRequirement(Long ruleId, RequirementRequest request);
    void deleteRequirement(Long businessId, Long ruleId);
}
