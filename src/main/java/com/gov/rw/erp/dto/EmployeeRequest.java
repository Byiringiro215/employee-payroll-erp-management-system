package com.gov.rw.erp.dto;

import com.gov.rw.erp.enums.EmployeeStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Request DTO for creating or updating an Employee.
 * Includes both personal information and core employment fields
 * (baseSalary, status) that drive payroll eligibility and computation.
 */
@Data
public class EmployeeRequest {

    // ---- Personal Information ----

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "First name must contain only letters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Last name must contain only letters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "District is required")
    @Size(max = 100, message = "District must not exceed 100 characters")
    private String district;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^(\\+?250|0)?7[2389]\\d{7}$",
             message = "Mobile must be a valid Rwanda phone number (e.g. 0780000001)")
    private String mobile;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    // ---- Core Employment Fields ----

    /**
     * Base salary — foundation for gross salary and all deduction computations.
     * Also stored on Employment for detailed HR records.
     */
    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "1.0", message = "Base salary must be greater than 0")
    private Double baseSalary;

    /**
     * Employment status — only ACTIVE employees are processed in payroll runs.
     */
    @NotNull(message = "Status is required (ACTIVE or INACTIVE)")
    private EmployeeStatus status;

    /** Optional: link this employee to an existing system User account */
    private Long userId;
}
