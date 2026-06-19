package com.gov.rw.erp.controller;

import com.gov.rw.erp.dto.ApiResponse;
import com.gov.rw.erp.dto.MessageResponse;
import com.gov.rw.erp.entity.Message;
import com.gov.rw.erp.repository.MessageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messaging")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final MessageRepository messageRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all messages (admin only)")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getAll() {
        List<MessageResponse> messages = messageRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Messages fetched", messages));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get messages for a specific employee")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getByEmployee(@PathVariable Long employeeId) {
        List<MessageResponse> messages = messageRepository.findByEmployeeId(employeeId)
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Messages fetched", messages));
    }

    @GetMapping("/payroll")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Get messages for a specific month/year")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getByMonthYear(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        List<MessageResponse> messages = messageRepository.findByMonthAndYear(month, year)
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Messages fetched", messages));
    }

    /** Map Message entity to a flat DTO — no circular references */
    private MessageResponse toResponse(Message m) {
        String employeeName = m.getEmployee() != null
                ? m.getEmployee().getFirstName() + " " + m.getEmployee().getLastName()
                : "";
        String employeeCode = (m.getEmployee() != null && m.getEmployee().getEmployment() != null)
                ? m.getEmployee().getEmployment().getEmployeeCode()
                : "";

        return MessageResponse.builder()
                .id(m.getId())
                .employeeId(m.getEmployee() != null ? m.getEmployee().getId() : null)
                .employeeName(employeeName)
                .employeeCode(employeeCode)
                .text(m.getText())
                .month(m.getMonth())
                .year(m.getYear())
                .dateTime(m.getDateTime())
                .build();
    }
}
