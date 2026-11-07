package com.cevicheria.platform.controller;

import java.time.LocalTime;
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

import com.cevicheria.platform.dto.BranchOpeningHourCreateRequest;
import com.cevicheria.platform.dto.BranchOpeningHourRequest;
import com.cevicheria.platform.dto.BranchOpeningHourResponse;
import com.cevicheria.platform.model.BranchOpeningHour;
import com.cevicheria.platform.model.id.BranchOpeningHourId;
import com.cevicheria.platform.service.BranchOpeningHourService;
import com.cevicheria.platform.service.BranchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/branch-opening-hours")
@RequiredArgsConstructor
public class BranchOpeningHourController {

    private final BranchOpeningHourService branchOpeningHourService;
    private final BranchService branchService;

    @GetMapping
    public List<BranchOpeningHourResponse> getAll(@RequestParam Long businessId, @RequestParam Long branchId) {
        return branchOpeningHourService.getByBranch(businessId, branchId).stream()
                .map(BranchOpeningHourController::toResponse)
                .toList();
    }

    @GetMapping("/{weekday}/{shiftNumber}")
    public BranchOpeningHourResponse getById(
            @RequestParam Long businessId,
            @RequestParam Long branchId,
            @PathVariable Integer weekday,
            @PathVariable Integer shiftNumber) {
        return toResponse(branchOpeningHourService.getById(new BranchOpeningHourId(businessId, branchId, weekday, shiftNumber)));
    }

    @PostMapping
    public ResponseEntity<BranchOpeningHourResponse> create(
            @RequestParam Long businessId,
            @RequestParam Long branchId,
            @RequestBody BranchOpeningHourCreateRequest request) {
        BranchOpeningHour value = toModel(businessId, branchId, request.weekday(), request.shiftNumber() == null ? 1 : request.shiftNumber(),
                        request.opensAt(), request.closesAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(branchOpeningHourService.save(value)));
    }

    @PutMapping("/{weekday}/{shiftNumber}")
    public BranchOpeningHourResponse update(
            @RequestParam Long businessId,
            @RequestParam Long branchId,
            @PathVariable Integer weekday,
            @PathVariable Integer shiftNumber,
            @RequestBody BranchOpeningHourRequest request) {
        BranchOpeningHour value = toModel(businessId, branchId, weekday, shiftNumber, request.opensAt(), request.closesAt());
        return toResponse(branchOpeningHourService.update(new BranchOpeningHourId(businessId, branchId, weekday, shiftNumber), value));
    }

    @DeleteMapping("/{weekday}/{shiftNumber}")
    public ResponseEntity<Void> delete(
            @RequestParam Long businessId,
            @RequestParam Long branchId,
            @PathVariable Integer weekday,
            @PathVariable Integer shiftNumber) {
        branchOpeningHourService.deleteById(new BranchOpeningHourId(businessId, branchId, weekday, shiftNumber));
        return ResponseEntity.noContent().build();
    }

    private BranchOpeningHour toModel(Long businessId, Long branchId, Integer weekday, Integer shiftNumber, LocalTime opensAt, LocalTime closesAt) {
        var branch = branchService.getById(businessId, branchId);
        return new BranchOpeningHour(branch.getBusiness(), branch, weekday, shiftNumber, opensAt, closesAt);
    }

    private static BranchOpeningHourResponse toResponse(BranchOpeningHour value) {
        return new BranchOpeningHourResponse(
                value.getId().getBusinessId(),
                value.getId().getBranchId(),
                value.getId().getWeekday(),
                value.getId().getShiftNumber(),
                value.getOpensAt(),
                value.getClosesAt());
    }
}
