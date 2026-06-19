package com.gov.rw.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clean response DTO for Message — avoids circular JSON serialization
 * by only exposing the fields the client actually needs.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String employeeCode;
    private String text;
    private Integer month;
    private Integer year;
    private LocalDateTime dateTime;
}
