package com.gov.rw.erp.entity;

import com.gov.rw.erp.enums.EmployeeStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "employments")
public class Employment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Generated employee code e.g. EMP-0001
    @Column(nullable = false, unique = true)
    private String employeeCode;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String institution;

    // Base salary used for payroll computation
    @Column(nullable = false)
    private Double baseSalary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeStatus status;

    @Column(nullable = false)
    private LocalDate joiningDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    public Employment() {}

    public Employment(Long id, String employeeCode, String department, String position, String institution, Double baseSalary, EmployeeStatus status, LocalDate joiningDate, Employee employee) {
        this.id = id;
        this.employeeCode = employeeCode;
        this.department = department;
        this.position = position;
        this.institution = institution;
        this.baseSalary = baseSalary;
        this.status = status;
        this.joiningDate = joiningDate;
        this.employee = employee;
    }

    public static EmploymentBuilder builder() {
        return new EmploymentBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public static class EmploymentBuilder {
        private Long id;
        private String employeeCode;
        private String department;
        private String position;
        private String institution;
        private Double baseSalary;
        private EmployeeStatus status;
        private LocalDate joiningDate;
        private Employee employee;

        public EmploymentBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EmploymentBuilder employeeCode(String employeeCode) {
            this.employeeCode = employeeCode;
            return this;
        }

        public EmploymentBuilder department(String department) {
            this.department = department;
            return this;
        }

        public EmploymentBuilder position(String position) {
            this.position = position;
            return this;
        }

        public EmploymentBuilder institution(String institution) {
            this.institution = institution;
            return this;
        }

        public EmploymentBuilder baseSalary(Double baseSalary) {
            this.baseSalary = baseSalary;
            return this;
        }

        public EmploymentBuilder status(EmployeeStatus status) {
            this.status = status;
            return this;
        }

        public EmploymentBuilder joiningDate(LocalDate joiningDate) {
            this.joiningDate = joiningDate;
            return this;
        }

        public EmploymentBuilder employee(Employee employee) {
            this.employee = employee;
            return this;
        }

        public Employment build() {
            return new Employment(id, employeeCode, department, position, institution, baseSalary, status, joiningDate, employee);
        }
    }
}
