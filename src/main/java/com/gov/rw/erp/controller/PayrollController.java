package com.gov.rw.erp.controller;

import com.gov.rw.erp.dto.*;
import com.gov.rw.erp.service.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
@Tag(name = "Payroll Management")
@SecurityRequirement(name = "bearerAuth")
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Generate payroll for all active employees for a given month/year")
    public ResponseEntity<ApiResponse<List<PayslipResponse>>> generate(@Valid @RequestBody PayrollRequest request) {
        List<PayslipResponse> result = payrollService.generatePayroll(request);
        return ResponseEntity.ok(ApiResponse.success("Payroll generated successfully", result));
    }

    @PostMapping("/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Approve payroll - marks payslips as PAID and sends messages")
    public ResponseEntity<ApiResponse<List<PayslipResponse>>> approve(@Valid @RequestBody PayrollRequest request) {
        List<PayslipResponse> result = payrollService.approvePayroll(request);
        return ResponseEntity.ok(ApiResponse.success("Payroll approved and messages sent", result));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Get all payslips for a given month/year")
    public ResponseEntity<ApiResponse<List<PayslipResponse>>> getByMonthYear(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(ApiResponse.success("Payroll fetched",
                payrollService.getPayrollByMonthYear(month, year)));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get all payslips for a specific employee")
    public ResponseEntity<ApiResponse<List<PayslipResponse>>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.success("Payslips fetched",
                payrollService.getEmployeePayslips(employeeId)));
    }

    @GetMapping("/employee/{employeeId}/payslip")
    @Operation(summary = "Get a specific payslip for an employee by month/year")
    public ResponseEntity<ApiResponse<PayslipResponse>> getPayslip(
            @PathVariable Long employeeId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(ApiResponse.success("Payslip fetched",
                payrollService.getPayslip(employeeId, month, year)));
    }
}
