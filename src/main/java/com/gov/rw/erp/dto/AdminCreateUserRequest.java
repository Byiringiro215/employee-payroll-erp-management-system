package com.gov.rw.erp.dto;

import com.gov.rw.erp.enums.EmployeeStatus;
import com.gov.rw.erp.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Admin-only request to create a full user + employee + employment record in one call.
 * Covers all fields from the users, employees, and employments tables.
 */
@Data
public class AdminCreateUserRequest {

    // ---- User Account Fields ----

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$",
             message = "Username can only contain letters, numbers, dots, hyphens and underscores")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /**
     * Password must contain at least one uppercase letter, one number, and one special character.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
        message = "Password must contain at least one uppercase letter, one number, and one special character (@$!%*?&#)"
    )
    private String password;

    @NotNull(message = "Role is required (ADMIN, MANAGER or EMPLOYEE)")
    private Role role;

    // ---- Employee Personal Information ----

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "First name must contain only letters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Last name must contain only letters")
    private String lastName;

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

    // ---- Employment / Professional Information ----

    @NotBlank(message = "Department is required")
    @Size(min = 2, max = 100)
    private String department;

    @NotBlank(message = "Position is required")
    @Size(min = 2, max = 100)
    private String position;

    @NotBlank(message = "Institution is required")
    @Size(min = 2, max = 150)
    private String institution;

    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "1.0", message = "Base salary must be greater than 0")
    private Double baseSalary;

    @NotNull(message = "Status is required (ACTIVE or INACTIVE)")
    private EmployeeStatus status;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;
}
