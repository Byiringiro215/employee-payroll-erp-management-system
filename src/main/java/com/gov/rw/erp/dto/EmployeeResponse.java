package com.gov.rw.erp.dto;

import com.gov.rw.erp.enums.EmployeeStatus;

import java.time.LocalDate;

public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String district;
    private String mobile;
    private LocalDate dateOfBirth;

    // Employment info
    private String employeeCode;
    private String department;
    private String position;
    private String institution;
    private Double baseSalary;
    private EmployeeStatus status;
    private LocalDate joiningDate;

    public EmployeeResponse() {}

    public EmployeeResponse(Long id, String firstName, String lastName, String email, String district, String mobile, LocalDate dateOfBirth, String employeeCode, String department, String position, String institution, Double baseSalary, EmployeeStatus status, LocalDate joiningDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.district = district;
        this.mobile = mobile;
        this.dateOfBirth = dateOfBirth;
        this.employeeCode = employeeCode;
        this.department = department;
        this.position = position;
        this.institution = institution;
        this.baseSalary = baseSalary;
        this.status = status;
        this.joiningDate = joiningDate;
    }

    public static EmployeeResponseBuilder builder() {
        return new EmployeeResponseBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public static class EmployeeResponseBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String district;
        private String mobile;
        private LocalDate dateOfBirth;
        private String employeeCode;
        private String department;
        private String position;
        private String institution;
        private Double baseSalary;
        private EmployeeStatus status;
        private LocalDate joiningDate;

        public EmployeeResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EmployeeResponseBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public EmployeeResponseBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public EmployeeResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public EmployeeResponseBuilder district(String district) {
            this.district = district;
            return this;
        }

        public EmployeeResponseBuilder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public EmployeeResponseBuilder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public EmployeeResponseBuilder employeeCode(String employeeCode) {
            this.employeeCode = employeeCode;
            return this;
        }

        public EmployeeResponseBuilder department(String department) {
            this.department = department;
            return this;
        }

        public EmployeeResponseBuilder position(String position) {
            this.position = position;
            return this;
        }

        public EmployeeResponseBuilder institution(String institution) {
            this.institution = institution;
            return this;
        }

        public EmployeeResponseBuilder baseSalary(Double baseSalary) {
            this.baseSalary = baseSalary;
            return this;
        }

        public EmployeeResponseBuilder status(EmployeeStatus status) {
            this.status = status;
            return this;
        }

        public EmployeeResponseBuilder joiningDate(LocalDate joiningDate) {
            this.joiningDate = joiningDate;
            return this;
        }

        public EmployeeResponse build() {
            return new EmployeeResponse(id, firstName, lastName, email, district, mobile, dateOfBirth, employeeCode, department, position, institution, baseSalary, status, joiningDate);
        }
    }
}
