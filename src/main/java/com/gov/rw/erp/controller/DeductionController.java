package com.gov.rw.erp.controller;

import com.gov.rw.erp.dto.*;
import com.gov.rw.erp.entity.Deduction;
import com.gov.rw.erp.service.DeductionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deductions")
@RequiredArgsConstructor
@Tag(name = "Deductions Management")
@SecurityRequirement(name = "bearerAuth")
public class DeductionController {

    private final DeductionService deductionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a deduction")
    public ResponseEntity<ApiResponse<Deduction>> create(@Valid @RequestBody DeductionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Deduction created", deductionService.create(request)));
    }

    @GetMapping
    @Operation(summary = "Get all deductions")
    public ResponseEntity<ApiResponse<List<Deduction>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Deductions fetched", deductionService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get deduction by ID")
    public ResponseEntity<ApiResponse<Deduction>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Deduction fetched", deductionService.findById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a deduction")
    public ResponseEntity<ApiResponse<Deduction>> update(
            @PathVariable Long id,
            @Valid @RequestBody DeductionRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Deduction updated", deductionService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a deduction")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        deductionService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Deduction deleted", null));
    }
}
