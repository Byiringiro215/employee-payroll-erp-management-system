package com.gov.rw.erp.controller;

import com.gov.rw.erp.dto.*;
import com.gov.rw.erp.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService authService;

    /**
     * Public self-registration endpoint.
     * No role field — all registrations default to EMPLOYEE.
     * Anyone can access this endpoint without a token.
     */
    @PostMapping("/register")
    @Operation(summary = "Self-register as a new employee user (role assigned automatically as EMPLOYEE)")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest request) {
        var user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful", user.getUsername()));
    }

    /**
     * Admin-only endpoint to create users with any role (ADMIN, MANAGER, EMPLOYEE).
     * Requires a valid JWT token with ADMIN role.
     */
    @PostMapping("/admin/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Admin only — create a user with a specific role (ADMIN / MANAGER / EMPLOYEE)")
    public ResponseEntity<ApiResponse<?>> adminCreateUser(@Valid @RequestBody AdminCreateUserRequest request) {
        var user = authService.adminCreateUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created with role " + user.getRole(), user.getUsername()));
    }

    /**
     * Login with username and password — returns JWT token.
     */
    @PostMapping("/login")
    @Operation(summary = "Login with username and password — returns JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }
}
