package com.gov.rw.erp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gov.rw.erp.enums.EmployeeStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Employee entity representing a government staff member.
 * Stores personal information and core employment fields (baseSalary, status)
 * required for payroll computation.
 */
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---- Personal Information ----

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    /** Unique work email address */
    @Column(nullable = false, unique = true)
    private String email;

    private String district;

    private String mobile;

    private LocalDate dateOfBirth;

    // ---- Employment Core Fields ----

    /**
     * Base salary used to compute gross salary, deductions and net salary.
     * Stored directly on Employee for quick access during payroll processing.
     */
    @Column(nullable = false)
    private Double baseSalary;

    /**
     * Employment status — only ACTIVE employees are included in payroll runs.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeStatus status;

    // ---- Relationships ----

    /** Optional link to a system User account for login access */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** Full employment/professional details (department, position, institution) */
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Employment employment;

    /**
     * One employee can have many payslips — one per month/year.
     * CascadeType.ALL ensures payslips are deleted when employee is removed.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payslip> payslips = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

    public Employee() {}

    public Employee(Long id, String firstName, String lastName, String email,
                    String district, String mobile, LocalDate dateOfBirth,
                    Double baseSalary, EmployeeStatus status,
                    User user, Employment employment) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.district = district;
        this.mobile = mobile;
        this.dateOfBirth = dateOfBirth;
        this.baseSalary = baseSalary;
        this.status = status;
        this.user = user;
        this.employment = employment;
    }

    public static EmployeeBuilder builder() {
        return new EmployeeBuilder();
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(Double baseSalary) { this.baseSalary = baseSalary; }

    public EmployeeStatus getStatus() { return status; }
    public void setStatus(EmployeeStatus status) { this.status = status; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Employment getEmployment() { return employment; }
    public void setEmployment(Employment employment) { this.employment = employment; }

    public List<Payslip> getPayslips() { return payslips; }
    public void setPayslips(List<Payslip> payslips) { this.payslips = payslips; }

    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }

    // ---- Builder ----

    public static class EmployeeBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String district;
        private String mobile;
        private LocalDate dateOfBirth;
        private Double baseSalary;
        private EmployeeStatus status;
        private User user;
        private Employment employment;

        public EmployeeBuilder id(Long id) { this.id = id; return this; }
        public EmployeeBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public EmployeeBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public EmployeeBuilder email(String email) { this.email = email; return this; }
        public EmployeeBuilder district(String district) { this.district = district; return this; }
        public EmployeeBuilder mobile(String mobile) { this.mobile = mobile; return this; }
        public EmployeeBuilder dateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
        public EmployeeBuilder baseSalary(Double baseSalary) { this.baseSalary = baseSalary; return this; }
        public EmployeeBuilder status(EmployeeStatus status) { this.status = status; return this; }
        public EmployeeBuilder user(User user) { this.user = user; return this; }
        public EmployeeBuilder employment(Employment employment) { this.employment = employment; return this; }

        public Employee build() {
            return new Employee(id, firstName, lastName, email, district, mobile,
                    dateOfBirth, baseSalary, status, user, employment);
        }
    }
}
