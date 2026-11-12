package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.ApplicationRequest;
import com.cevicheria.platform.dto.ApplicationView;
import com.cevicheria.platform.dto.PaymentRequest;
import com.cevicheria.platform.dto.PaymentView;
import java.util.List;

public interface BillingPaymentsService {
    List<PaymentView> payments(Long businessId, Long branchId);
    PaymentView payment(Long businessId, Long branchId, Long id);
    PaymentView createPayment(PaymentRequest request);
    PaymentView verifyPayment(Long businessId, Long branchId, Long id);
    List<ApplicationView> applications(Long businessId, Long branchId);
    ApplicationView application(Long businessId, Long branchId, Long id);
    ApplicationView createApplication(ApplicationRequest request);
    ApplicationView reverseApplication(Long businessId, Long branchId, Long id);
}
