package com.cevicheria.platform.controller;

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
import com.cevicheria.platform.service.BillingSalesService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/billing")
public class BillingSalesController {
    private final BillingSalesService service;

    @GetMapping("/bills")
    public List<BillView> bills(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.bills(businessId, branchId);
    }
    @GetMapping("/bills/{id}")
    public BillView bill(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.bill(businessId, branchId, id);
    }
    @PostMapping("/bills")
    public ResponseEntity<BillView> createBill(@RequestBody BillRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createBill(r));
    }
    @DeleteMapping("/bills/{id}")
    public ResponseEntity<Void> cancelBill(@RequestParam Long businessId, @RequestParam Long branchId,
                                           @PathVariable Long id) {
        service.cancelBill(businessId, branchId, id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/bills/{billId}/items")
    public List<BillItemView> billItems(@RequestParam Long businessId, @RequestParam Long branchId,
                                        @PathVariable Long billId) {
        return service.billItems(businessId, branchId, billId);
    }
    @PostMapping("/bill-items")
    public ResponseEntity<BillItemView> addBillItem(@RequestBody BillItemRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addBillItem(r));
    }
    @DeleteMapping("/bill-items/{id}")
    public ResponseEntity<Void> deleteBillItem(@RequestParam Long businessId, @RequestParam Long branchId,
                                                @PathVariable Long id) {
        service.deleteBillItem(businessId, branchId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/receipts")
    public List<ReceiptView> receipts(@RequestParam Long businessId, @RequestParam Long branchId) {
        return service.receipts(businessId, branchId);
    }
    @GetMapping("/receipts/{id}")
    public ReceiptView receipt(@RequestParam Long businessId, @RequestParam Long branchId, @PathVariable Long id) {
        return service.receipt(businessId, branchId, id);
    }
    @PostMapping("/receipts")
    public ResponseEntity<ReceiptView> createReceipt(@RequestBody ReceiptRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReceipt(r));
    }
    @PutMapping("/receipts/{id}/issue-local")
    public ReceiptView issueLocally(@RequestParam Long businessId, @RequestParam Long branchId,
                                    @PathVariable Long id) {
        return service.issueLocally(businessId, branchId, id);
    }
    @GetMapping("/receipts/{receiptId}/items")
    public List<ReceiptItemView> receiptItems(@RequestParam Long businessId, @RequestParam Long branchId,
                                              @PathVariable Long receiptId) {
        return service.receiptItems(businessId, branchId, receiptId);
    }
    @PostMapping("/receipt-items")
    public ResponseEntity<ReceiptItemView> addReceiptItem(@RequestBody ReceiptItemRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addReceiptItem(r));
    }
    @DeleteMapping("/receipt-items/{id}")
    public ResponseEntity<Void> deleteReceiptItem(@RequestParam Long businessId, @RequestParam Long branchId,
                                                   @PathVariable Long id) {
        service.deleteReceiptItem(businessId, branchId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/receipts/{receiptId}/issuer")
    public IssuerView issuer(@RequestParam Long businessId, @RequestParam Long branchId,
                             @PathVariable Long receiptId) {
        return service.issuer(businessId, branchId, receiptId);
    }
    @PostMapping("/receipt-issuers")
    public ResponseEntity<IssuerView> createIssuer(@RequestBody IssuerRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createIssuer(r));
    }
    @GetMapping("/receipts/{receiptId}/customer-snapshot")
    public CustomerSnapshotView customerSnapshot(@RequestParam Long businessId, @RequestParam Long branchId,
                                                  @PathVariable Long receiptId) {
        return service.customerSnapshot(businessId, branchId, receiptId);
    }
    @PostMapping("/receipt-customers")
    public ResponseEntity<CustomerSnapshotView> createCustomerSnapshot(@RequestBody CustomerSnapshotRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCustomerSnapshot(r));
    }
    @GetMapping("/receipts/{receiptId}/integration-attempts")
    public List<AttemptView> attempts(@RequestParam Long businessId, @RequestParam Long branchId,
                                     @PathVariable Long receiptId) {
        return service.attempts(businessId, branchId, receiptId);
    }
    @PostMapping("/receipt-integration-tests")
    public ResponseEntity<AttemptView> recordLocalAttempt(@RequestBody AttemptRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.recordLocalAttempt(r));
    }
}
