package com.cevicheria.platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cevicheria.platform.model.BranchProduct;
import com.cevicheria.platform.model.enums.SalesChannel;
import com.cevicheria.platform.service.BranchProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/branch-products")
@RequiredArgsConstructor
public class BranchProductController {

    private final BranchProductService branchProductService;

    @GetMapping
    public List<BranchProduct> getAll(@RequestParam Long businessId) {
        return branchProductService.getAllByBusiness(businessId);
    }

    @GetMapping("/{id}")
    public BranchProduct getById(@PathVariable Long id) {
        return branchProductService.getById(id);
    }

    @GetMapping("/branch/{branchId}")
    public List<BranchProduct> getByBranch(@RequestParam Long businessId, @PathVariable Long branchId) {
        return branchProductService.getByBranch(businessId, branchId);
    }

    @GetMapping("/branch/{branchId}/visible")
    public List<BranchProduct> getVisibleByBranch(@RequestParam Long businessId, @PathVariable Long branchId) {
        return branchProductService.getVisibleByBranch(businessId, branchId);
    }

    @GetMapping("/branch/{branchId}/available")
    public List<BranchProduct> getAvailableByBranch(@RequestParam Long businessId, @PathVariable Long branchId) {
        return branchProductService.getAvailableByBranch(businessId, branchId);
    }

    @GetMapping("/product/{productId}")
    public List<BranchProduct> getByProduct(@RequestParam Long businessId, @PathVariable Long productId) {
        return branchProductService.getByProduct(businessId, productId);
    }

    @GetMapping("/branch/{branchId}/product/{productId}/channel/{salesChannel}")
    public BranchProduct getByChannel(
            @RequestParam Long businessId,
            @PathVariable Long branchId,
            @PathVariable Long productId,
            @PathVariable SalesChannel salesChannel) {
        return branchProductService.getByBranchProductAndChannel(
                businessId, branchId, productId, salesChannel);
    }

    @PostMapping
    public ResponseEntity<BranchProduct> create(@RequestBody BranchProduct branchProduct) {
        return ResponseEntity.status(HttpStatus.CREATED).body(branchProductService.save(branchProduct));
    }

    @PutMapping("/{id}")
    public BranchProduct update(@PathVariable Long id, @RequestBody BranchProduct branchProduct) {
        return branchProductService.update(id, branchProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        branchProductService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}