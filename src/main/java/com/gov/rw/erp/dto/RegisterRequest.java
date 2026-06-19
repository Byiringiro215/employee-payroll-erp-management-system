package com.gov.rw.erp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Public self-registration request.
 * Role is NOT exposed — all self-registered users get EMPLOYEE by default.
 */
@Data
public class RegisterRequest {

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
     * Password must be at least 8 characters and contain:
     * - At least one uppercase letter
     * - At least one digit
     * - At least one special character (@$!%*?&#)
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
        message = "Password must contain at least one uppercase letter, one number, and one special character (@$!%*?&#)"
    )
    private String password;
}
