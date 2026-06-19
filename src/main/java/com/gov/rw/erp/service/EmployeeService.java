package com.gov.rw.erp.service;

import com.gov.rw.erp.dto.*;
import com.gov.rw.erp.entity.*;
import com.gov.rw.erp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmploymentRepository employmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        // Check email uniqueness before creating
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Employee with this email already exists");
        }

        // Build employee with personal info + core salary/status fields
        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .district(request.getDistrict())
                .mobile(request.getMobile())
                .dateOfBirth(request.getDateOfBirth())
                .baseSalary(request.getBaseSalary())   // stored on Employee for payroll access
                .status(request.getStatus())            // ACTIVE/INACTIVE controls payroll eligibility
                .build();

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            employee.setUser(user);
        }

        return mapToResponse(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponse addEmployment(Long employeeId, EmploymentRequest request) {
        Employee employee = getEmployeeEntity(employeeId);

        if (employmentRepository.findByEmployeeId(employeeId).isPresent()) {
            throw new IllegalArgumentException("Employment details already exist for this employee");
        }

        String code = generateEmployeeCode();

        Employment employment = Employment.builder()
                .employeeCode(code)
                .department(request.getDepartment())
                .position(request.getPosition())
                .institution(request.getInstitution())
                .baseSalary(request.getBaseSalary())
                .status(request.getStatus())
                .joiningDate(request.getJoiningDate())
                .employee(employee)
                .build();

        employmentRepository.save(employment);

        // Keep baseSalary and status in sync on the Employee entity
        employee.setBaseSalary(request.getBaseSalary());
        employee.setStatus(request.getStatus());
        employeeRepository.save(employee);

        return mapToResponse(employeeRepository.findById(employeeId).orElseThrow());
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = getEmployeeEntity(id);
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDistrict(request.getDistrict());
        employee.setMobile(request.getMobile());
        employee.setDateOfBirth(request.getDateOfBirth());
        return mapToResponse(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponse updateEmployment(Long employeeId, EmploymentRequest request) {
        Employment employment = employmentRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employment not found for employee"));
        employment.setDepartment(request.getDepartment());
        employment.setPosition(request.getPosition());
        employment.setInstitution(request.getInstitution());
        employment.setBaseSalary(request.getBaseSalary());
        employment.setStatus(request.getStatus());
        employment.setJoiningDate(request.getJoiningDate());
        employmentRepository.save(employment);

        // Keep Employee entity in sync with updated employment fields
        Employee employee = getEmployeeEntity(employeeId);
        employee.setBaseSalary(request.getBaseSalary());
        employee.setStatus(request.getStatus());
        employeeRepository.save(employee);

        return mapToResponse(employeeRepository.findById(employeeId).orElseThrow());
    }

    public EmployeeResponse getEmployee(Long id) {
        return mapToResponse(getEmployeeEntity(id));
    }

    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteEmployee(Long id) {
        getEmployeeEntity(id);
        employeeRepository.deleteById(id);
    }

    private Employee getEmployeeEntity(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + id));
    }

    private String generateEmployeeCode() {
        long count = employmentRepository.count() + 1;
        return String.format("EMP-%04d", count);
    }

    public EmployeeResponse mapToResponse(Employee employee) {
        EmployeeResponse.EmployeeResponseBuilder builder = EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .district(employee.getDistrict())
                .mobile(employee.getMobile())
                .dateOfBirth(employee.getDateOfBirth());

        Employment emp = employee.getEmployment();
        if (emp != null) {
            builder.employeeCode(emp.getEmployeeCode())
                    .department(emp.getDepartment())
                    .position(emp.getPosition())
                    .institution(emp.getInstitution())
                    .baseSalary(emp.getBaseSalary())
                    .status(emp.getStatus())
                    .joiningDate(emp.getJoiningDate());
        }

        return builder.build();
    }
}
