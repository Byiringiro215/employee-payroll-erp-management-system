package com.gov.rw.erp;

import com.gov.rw.erp.entity.*;
import com.gov.rw.erp.enums.*;
import com.gov.rw.erp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class ErpApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ErpApplication.class);
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final EmploymentRepository employmentRepository;
    private final DeductionRepository deductionRepository;
    private final PasswordEncoder passwordEncoder;

    public ErpApplication(UserRepository userRepository, EmployeeRepository employeeRepository, EmploymentRepository employmentRepository, DeductionRepository deductionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.employmentRepository = employmentRepository;
        this.deductionRepository = deductionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(ErpApplication.class, args);
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedDeductions();
        seedSampleEmployees();
    }

    private void seedUsers() {
        if (userRepository.existsByUsername("admin")) return;

        userRepository.save(User.builder()
                .username("admin")
                .email("admin@gov.rw")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build());

        userRepository.save(User.builder()
                .username("manager")
                .email("manager@gov.rw")
                .password(passwordEncoder.encode("manager123"))
                .role(Role.MANAGER)
                .build());

        log.info("Seeded default users: admin / manager");
    }

    private void seedDeductions() {
        if (deductionRepository.count() > 0) return;

        deductionRepository.save(Deduction.builder().deductionName("EmployeeTax").percentage(30.0).build());
        deductionRepository.save(Deduction.builder().deductionName("Pension").percentage(6.0).build());
        deductionRepository.save(Deduction.builder().deductionName("MedicalInsurance").percentage(5.0).build());
        deductionRepository.save(Deduction.builder().deductionName("Others").percentage(4.0).build());
        deductionRepository.save(Deduction.builder().deductionName("House").percentage(14.0).build());
        deductionRepository.save(Deduction.builder().deductionName("Transport").percentage(14.0).build());

        log.info("Seeded default deductions");
    }

    private void seedSampleEmployees() {
        if (employeeRepository.count() > 0) return;

        // Employee 1 - Mugabo Javis (baseSalary=70000, ACTIVE)
        Employee emp1 = employeeRepository.save(Employee.builder()
                .firstName("Mugabo")
                .lastName("Javis")
                .email("mugabo.javis@rca.gov.rw")
                .district("Kigali")
                .mobile("0780000001")
                .dateOfBirth(LocalDate.of(1990, 3, 15))
                .baseSalary(70000.0)
                .status(EmployeeStatus.ACTIVE)
                .build());

        employmentRepository.save(Employment.builder()
                .employeeCode("EMP-0001")
                .department("Finance")
                .position("Accountant")
                .institution("RCA")
                .baseSalary(70000.0)
                .status(EmployeeStatus.ACTIVE)
                .joiningDate(LocalDate.of(2020, 1, 10))
                .employee(emp1)
                .build());

        // Employee 2 - Michou Michell (baseSalary=35000, ACTIVE)
        Employee emp2 = employeeRepository.save(Employee.builder()
                .firstName("Michou")
                .lastName("Michell")
                .email("michou.michell@rca.gov.rw")
                .district("Musanze")
                .mobile("0780000002")
                .dateOfBirth(LocalDate.of(1993, 7, 20))
                .baseSalary(35000.0)
                .status(EmployeeStatus.ACTIVE)
                .build());

        employmentRepository.save(Employment.builder()
                .employeeCode("EMP-0002")
                .department("HR")
                .position("HR Officer")
                .institution("RCA")
                .baseSalary(35000.0)
                .status(EmployeeStatus.ACTIVE)
                .joiningDate(LocalDate.of(2021, 5, 1))
                .employee(emp2)
                .build());

        log.info("Seeded sample employees");
    }
}
