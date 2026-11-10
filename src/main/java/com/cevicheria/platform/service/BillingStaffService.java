package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.IncentiveRequest;
import com.cevicheria.platform.dto.IncentiveView;
import com.cevicheria.platform.dto.SettlementRequest;
import com.cevicheria.platform.dto.SettlementView;
import java.util.List;

public interface BillingStaffService {
    List<SettlementView> settlements(Long businessId, Long branchId);
    SettlementView settlement(Long businessId, Long branchId, Long id);
    SettlementView createSettlement(SettlementRequest request);
    SettlementView approveSettlement(Long businessId, Long branchId, Long id);
    SettlementView paySettlement(Long businessId, Long branchId, Long id, Long paymentId);
    List<IncentiveView> incentives(Long businessId, Long branchId);
    IncentiveView incentive(Long businessId, Long branchId, Long id);
    IncentiveView createIncentive(IncentiveRequest request);
}
