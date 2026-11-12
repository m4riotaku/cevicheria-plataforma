package com.cevicheria.platform.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cevicheria.platform.model.BatchControl;
import com.cevicheria.platform.service.BatchControlService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/batch-controls")
@RequiredArgsConstructor
public class BatchControlController {
    private final BatchControlService controlService;

    @GetMapping
    public List<BatchControl> getAll(@RequestParam Long businessId) { return controlService.getAllByBusiness(businessId); }

    @GetMapping("/batch/{batchId}")
    public List<BatchControl> getByBatch(@RequestParam Long businessId, @PathVariable Long batchId) { return controlService.getByBatch(businessId, batchId); }

    @GetMapping("/{id}")
    public BatchControl getById(@PathVariable Long id) { return controlService.getById(id); }

    @PostMapping
    public ResponseEntity<BatchControl> create(@RequestBody BatchControl control) { return ResponseEntity.status(HttpStatus.CREATED).body(controlService.save(control)); }

    @PutMapping("/{id}")
    public BatchControl update(@PathVariable Long id, @RequestBody BatchControl control) { return controlService.update(id, control); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { controlService.deleteById(id); return ResponseEntity.noContent().build(); }
}