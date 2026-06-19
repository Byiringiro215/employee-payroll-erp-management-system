package com.gov.rw.erp.service;

import com.gov.rw.erp.dto.PayrollRequest;
import com.gov.rw.erp.dto.PayslipResponse;
import com.gov.rw.erp.entity.*;
import com.gov.rw.erp.enums.EmployeeStatus;
import com.gov.rw.erp.enums.PayslipStatus;
import com.gov.rw.erp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PayrollService handles all payroll computation and approval logic.
 *
 * Salary formulas:
 *   grossSalary = baseSalary + (baseSalary × 14%) + (baseSalary × 14%)
 *   netSalary   = baseSalary - (tax 30% + pension 6% + medical 5% + others 5%)
 *               = baseSalary - (baseSalary × 46%)
 *
 * Only ACTIVE employees are processed. Duplicate payslips per month/year are blocked.
 */
@Service
@RequiredArgsConstructor
public class PayrollService {

    private final PayslipRepository payslipRepository;
    private final EmploymentRepository employmentRepository;
    private final MessageRepository messageRepository;
    private final DeductionService deductionService;

    /**
     * Generates payslips for all ACTIVE employees for a given month/year.
     * Skips any employee that already has a payslip for that period (duplicate prevention).
     *
     * @param request contains month and year for the payroll run
     * @return list of generated payslip responses
     */
    @Transactional
    public List<PayslipResponse> generatePayroll(PayrollRequest request) {

        // Only process ACTIVE employees — inactive ones are excluded from payroll
        List<Employment> activeEmployments = employmentRepository.findByStatus(EmployeeStatus.ACTIVE);

        if (activeEmployments.isEmpty()) {
            throw new IllegalStateException("No active employees found for payroll processing");
        }

        // Load all deduction rates once from DB (avoids repeated queries per employee)
        double taxRate       = deductionService.getPercentage("EmployeeTax");      // 30%
        double pensionRate   = deductionService.getPercentage("Pension");           // 6%
        double medicalRate   = deductionService.getPercentage("MedicalInsurance"); // 5%
        double othersRate    = deductionService.getPercentage("Others");            // 5%
        double houseRate     = deductionService.getPercentage("House");             // 14%
        double transportRate = deductionService.getPercentage("Transport");         // 14%

        List<Payslip> generated = new ArrayList<>();

        for (Employment employment : activeEmployments) {
            Employee employee = employment.getEmployee();

            // Prevent duplicate payslip: skip if one already exists for this employee/month/year
            if (payslipRepository.existsByEmployeeIdAndMonthAndYear(
                    employee.getId(), request.getMonth(), request.getYear())) {
                continue;
            }

            double base = employment.getBaseSalary();

            // ---- Gross salary calculation ----
            // grossSalary = baseSalary + House allowance (14%) + Transport allowance (14%)
            double house     = round(base * houseRate / 100);
            double transport = round(base * transportRate / 100);
            double gross     = base + house + transport;

            // ---- Deductions calculation ----
            // Each deduction is computed as a percentage of baseSalary (not gross)
            double tax     = round(base * taxRate / 100);       // 30% of base
            double pension = round(base * pensionRate / 100);   // 6%  of base
            double medical = round(base * medicalRate / 100);   // 5%  of base
            double others  = round(base * othersRate / 100);    // 5%  of base

            double totalDeductions = tax + pension + medical + others;

            // Safety check: total deductions must not exceed gross salary
            if (totalDeductions > gross) {
                throw new IllegalStateException(
                    "Total deductions exceed gross salary for: " + employment.getEmployeeCode());
            }

            // ---- Net salary calculation ----
            // netSalary = baseSalary - total deductions (tax + pension + medical + others)
            double net = base - totalDeductions;

            // Build and persist the payslip with PENDING status
            Payslip payslip = Payslip.builder()
                    .employee(employee)
                    .month(request.getMonth())
                    .year(request.getYear())
                    .baseSalary(base)
                    .houseAmount(house)
                    .transportAmount(transport)
                    .grossSalary(gross)
                    .employeeTax(tax)
                    .pension(pension)
                    .medicalInsurance(medical)
                    .others(others)
                    .netSalary(net)
                    .status(PayslipStatus.PENDING) // starts as PENDING until approved
                    .build();

            generated.add(payslipRepository.save(payslip));
        }

        return generated.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Approves payroll for a given month/year.
     * Updates all PENDING payslips to PAID and generates a notification message per employee.
     *
     * @param request contains month and year of the payroll to approve
     * @return list of approved payslip responses
     */
    @Transactional
    public List<PayslipResponse> approvePayroll(PayrollRequest request) {
        List<Payslip> payslips = payslipRepository.findByMonthAndYear(
                request.getMonth(), request.getYear());

        if (payslips.isEmpty()) {
            throw new IllegalArgumentException(
                "No payslips found for " + request.getMonth() + "/" + request.getYear());
        }

        List<Payslip> approved = new ArrayList<>();

        for (Payslip payslip : payslips) {
            if (payslip.getStatus() == PayslipStatus.PENDING) {

                // Update payslip status from PENDING → PAID
                payslip.setStatus(PayslipStatus.PAID);
                payslipRepository.save(payslip);

                // Auto-generate and persist the "Dear..." notification message
                generateAndSaveMessage(payslip);

                approved.add(payslip);
            }
        }

        return approved.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Retrieves all payslips for a specific month/year (admin/manager view).
     */
    public List<PayslipResponse> getPayrollByMonthYear(Integer month, Integer year) {
        return payslipRepository.findByMonthAndYear(month, year)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Retrieves all payslips for a specific employee across all periods.
     */
    public List<PayslipResponse> getEmployeePayslips(Long employeeId) {
        return payslipRepository.findByEmployeeId(employeeId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Retrieves a single payslip for an employee for a specific month/year.
     */
    public PayslipResponse getPayslip(Long employeeId, Integer month, Integer year) {
        Payslip payslip = payslipRepository
                .findByEmployeeIdAndMonthAndYear(employeeId, month, year)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Payslip not found for employee " + employeeId + " - " + month + "/" + year));
        return mapToResponse(payslip);
    }

    // ---- Private helpers ----

    /**
     * Builds and persists the salary notification message for an employee.
     * Format: "Dear <FIRSTNAME> Your salary of MM/YYYY from <INSTITUTION>
     *          <AMOUNT> FRW has been credited to your <EMP_CODE> account Successfully."
     */
    private void generateAndSaveMessage(Payslip payslip) {
        Employee employee   = payslip.getEmployee();
        Employment employment = employee.getEmployment();
        String institution  = employment != null ? employment.getInstitution() : "Government of Rwanda";
        String empCode      = employment != null ? employment.getEmployeeCode() : "N/A";

        // Build the "Dear..." message string as specified in the requirements
        String messageText = String.format(
            "Dear %s Your salary of %02d/%d from %s %.2f FRW has been credited to your %s account Successfully.",
            employee.getFirstName(),
            payslip.getMonth(),
            payslip.getYear(),
            institution,
            payslip.getNetSalary(),
            empCode
        );

        // Persist the message record — this also acts as a notification log
        Message message = Message.builder()
                .employee(employee)
                .text(messageText)
                .month(payslip.getMonth())
                .year(payslip.getYear())
                .dateTime(LocalDateTime.now())
                .build();

        messageRepository.save(message);
    }

    /** Maps a Payslip entity to its response DTO */
    private PayslipResponse mapToResponse(Payslip p) {
        Employee employee    = p.getEmployee();
        Employment employment = employee.getEmployment();

        // Look up the notification message for this payslip (only exists after approval)
        String notificationMsg = messageRepository
                .findByEmployeeIdAndMonthAndYear(employee.getId(), p.getMonth(), p.getYear())
                .map(m -> m.getText())
                .orElse(null);

        return PayslipResponse.builder()
                .id(p.getId())
                .employeeCode(employment != null ? employment.getEmployeeCode() : "")
                .employeeName(employee.getFirstName() + " " + employee.getLastName())
                .month(p.getMonth())
                .year(p.getYear())
                .baseSalary(p.getBaseSalary())
                .houseAmount(p.getHouseAmount())
                .transportAmount(p.getTransportAmount())
                .grossSalary(p.getGrossSalary())
                .employeeTax(p.getEmployeeTax())
                .pension(p.getPension())
                .medicalInsurance(p.getMedicalInsurance())
                .others(p.getOthers())
                .netSalary(p.getNetSalary())
                .status(p.getStatus())
                .institution(employment != null ? employment.getInstitution() : "")
                .notificationMessage(notificationMsg)
                .build();
    }

    /** Rounds a value to 2 decimal places to avoid floating point drift */
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
