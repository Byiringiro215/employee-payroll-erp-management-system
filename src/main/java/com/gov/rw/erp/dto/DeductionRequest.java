package com.gov.rw.erp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DeductionRequest {

    @NotBlank(message = "Deduction name is required")
    @Size(min = 2, max = 100, message = "Deduction name must be between 2 and 100 characters")
    private String deductionName;

    @NotNull(message = "Percentage is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Percentage cannot be negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Percentage cannot exceed 100")
    private Double percentage;
}
