package com.gov.rw.erp.dto;

import com.gov.rw.erp.enums.EmployeeStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmploymentRequest {

    @NotBlank(message = "Department is required")
    @Size(min = 2, max = 100, message = "Department must be between 2 and 100 characters")
    private String department;

    @NotBlank(message = "Position is required")
    @Size(min = 2, max = 100, message = "Position must be between 2 and 100 characters")
    private String position;

    @NotBlank(message = "Institution is required")
    @Size(min = 2, max = 150, message = "Institution must be between 2 and 150 characters")
    private String institution;

    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "1.0", message = "Base salary must be greater than 0")
    @DecimalMax(value = "10000000.0", message = "Base salary seems unrealistically high")
    private Double baseSalary;

    @NotNull(message = "Status is required (ACTIVE or INACTIVE)")
    private EmployeeStatus status;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;
}
