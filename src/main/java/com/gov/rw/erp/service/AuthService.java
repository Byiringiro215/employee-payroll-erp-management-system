package com.gov.rw.erp.service;

import com.gov.rw.erp.config.JwtUtil;
import com.gov.rw.erp.dto.*;
import com.gov.rw.erp.entity.*;
import com.gov.rw.erp.enums.Role;
import com.gov.rw.erp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final EmploymentRepository employmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Public self-registration.
     * Role is always EMPLOYEE — never exposed to the caller.
     * Only creates a User account; employee profile added separately.
     */
    @Transactional
    public User register(RegisterRequest request) {
        validateUnique(request.getUsername(), request.getEmail());

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.EMPLOYEE)
                .build();

        return userRepository.save(user);
    }

    /**
     * Admin-only full account creation.
     * Creates User + Employee + Employment records in one transaction.
     * All personal, account, and employment data is set at creation time.
     */
    @Transactional
    public User adminCreateUser(AdminCreateUserRequest request) {
        validateUnique(request.getUsername(), request.getEmail());

        // Check employee email uniqueness separately (employees table has its own email)
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("An employee profile with this email already exists");
        }

        // 1. Create the User account
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        user = userRepository.save(user);

        // 2. Create the Employee profile linked to the User
        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .district(request.getDistrict())
                .mobile(request.getMobile())
                .dateOfBirth(request.getDateOfBirth())
                .baseSalary(request.getBaseSalary())
                .status(request.getStatus())
                .user(user)
                .build();
        employee = employeeRepository.save(employee);

        // 3. Create Employment details with auto-generated employee code
        String employeeCode = generateEmployeeCode();
        Employment employment = Employment.builder()
                .employeeCode(employeeCode)
                .department(request.getDepartment())
                .position(request.getPosition())
                .institution(request.getInstitution())
                .baseSalary(request.getBaseSalary())
                .status(request.getStatus())
                .joiningDate(request.getJoiningDate())
                .employee(employee)
                .build();
        employmentRepository.save(employment);

        return user;
    }

    /**
     * Login with email + password — returns a JWT token.
     */
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getRole().name());
    }

    // ---- Private helpers ----

    private void validateUnique(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }
    }

    private String generateEmployeeCode() {
        long count = employmentRepository.count() + 1;
        return String.format("EMP-%04d", count);
    }
}
