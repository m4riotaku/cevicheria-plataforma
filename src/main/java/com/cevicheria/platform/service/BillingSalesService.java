package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.AttemptRequest;
import com.cevicheria.platform.dto.AttemptView;
import com.cevicheria.platform.dto.BillItemRequest;
import com.cevicheria.platform.dto.BillItemView;
import com.cevicheria.platform.dto.BillRequest;
import com.cevicheria.platform.dto.BillView;
import com.cevicheria.platform.dto.CustomerSnapshotRequest;
import com.cevicheria.platform.dto.CustomerSnapshotView;
import com.cevicheria.platform.dto.IssuerRequest;
import com.cevicheria.platform.dto.IssuerView;
import com.cevicheria.platform.dto.ReceiptItemRequest;
import com.cevicheria.platform.dto.ReceiptItemView;
import com.cevicheria.platform.dto.ReceiptRequest;
import com.cevicheria.platform.dto.ReceiptView;
import java.util.List;

public interface BillingSalesService {
    List<BillView> bills(Long businessId, Long branchId);
    BillView bill(Long businessId, Long branchId, Long id);
    BillView createBill(BillRequest request);
    void cancelBill(Long businessId, Long branchId, Long id);
    List<BillItemView> billItems(Long businessId, Long branchId, Long billId);
    BillItemView addBillItem(BillItemRequest request);
    void deleteBillItem(Long businessId, Long branchId, Long id);

    List<ReceiptView> receipts(Long businessId, Long branchId);
    ReceiptView receipt(Long businessId, Long branchId, Long id);
    ReceiptView createReceipt(ReceiptRequest request);
    ReceiptView issueLocally(Long businessId, Long branchId, Long id);
    List<ReceiptItemView> receiptItems(Long businessId, Long branchId, Long receiptId);
    ReceiptItemView addReceiptItem(ReceiptItemRequest request);
    void deleteReceiptItem(Long businessId, Long branchId, Long id);

    IssuerView issuer(Long businessId, Long branchId, Long receiptId);
    IssuerView createIssuer(IssuerRequest request);
    CustomerSnapshotView customerSnapshot(Long businessId, Long branchId, Long receiptId);
    CustomerSnapshotView createCustomerSnapshot(CustomerSnapshotRequest request);
    List<AttemptView> attempts(Long businessId, Long branchId, Long receiptId);
    AttemptView recordLocalAttempt(AttemptRequest request);
}
