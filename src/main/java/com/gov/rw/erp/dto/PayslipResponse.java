package com.gov.rw.erp.dto;

import com.gov.rw.erp.enums.PayslipStatus;

public class PayslipResponse {
    private Long id;
    private String employeeCode;
    private String employeeName;
    private Integer month;
    private Integer year;
    private Double baseSalary;
    private Double houseAmount;
    private Double transportAmount;
    private Double grossSalary;
    private Double employeeTax;
    private Double pension;
    private Double medicalInsurance;
    private Double others;
    private Double netSalary;
    private PayslipStatus status;
    private String institution;
    private String notificationMessage; // "Dear..." message sent on approval

    public PayslipResponse() {}

    public PayslipResponse(Long id, String employeeCode, String employeeName, Integer month, Integer year,
                           Double baseSalary, Double houseAmount, Double transportAmount, Double grossSalary,
                           Double employeeTax, Double pension, Double medicalInsurance, Double others,
                           Double netSalary, PayslipStatus status, String institution, String notificationMessage) {
        this.id = id;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.month = month;
        this.year = year;
        this.baseSalary = baseSalary;
        this.houseAmount = houseAmount;
        this.transportAmount = transportAmount;
        this.grossSalary = grossSalary;
        this.employeeTax = employeeTax;
        this.pension = pension;
        this.medicalInsurance = medicalInsurance;
        this.others = others;
        this.netSalary = netSalary;
        this.status = status;
        this.institution = institution;
        this.notificationMessage = notificationMessage;
    }

    public static PayslipResponseBuilder builder() {
        return new PayslipResponseBuilder();
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Double getHouseAmount() {
        return houseAmount;
    }

    public void setHouseAmount(Double houseAmount) {
        this.houseAmount = houseAmount;
    }

    public Double getTransportAmount() {
        return transportAmount;
    }

    public void setTransportAmount(Double transportAmount) {
        this.transportAmount = transportAmount;
    }

    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Double grossSalary) {
        this.grossSalary = grossSalary;
    }

    public Double getEmployeeTax() {
        return employeeTax;
    }

    public void setEmployeeTax(Double employeeTax) {
        this.employeeTax = employeeTax;
    }

    public Double getPension() {
        return pension;
    }

    public void setPension(Double pension) {
        this.pension = pension;
    }

    public Double getMedicalInsurance() {
        return medicalInsurance;
    }

    public void setMedicalInsurance(Double medicalInsurance) {
        this.medicalInsurance = medicalInsurance;
    }

    public Double getOthers() {
        return others;
    }

    public void setOthers(Double others) {
        this.others = others;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public PayslipStatus getStatus() {
        return status;
    }

    public void setStatus(PayslipStatus status) {
        this.status = status;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public static class PayslipResponseBuilder {
        private Long id;
        private String employeeCode;
        private String employeeName;
        private Integer month;
        private Integer year;
        private Double baseSalary;
        private Double houseAmount;
        private Double transportAmount;
        private Double grossSalary;
        private Double employeeTax;
        private Double pension;
        private Double medicalInsurance;
        private Double others;
        private Double netSalary;
        private PayslipStatus status;
        private String institution;
        private String notificationMessage;

        public PayslipResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PayslipResponseBuilder employeeCode(String employeeCode) {
            this.employeeCode = employeeCode;
            return this;
        }

        public PayslipResponseBuilder employeeName(String employeeName) {
            this.employeeName = employeeName;
            return this;
        }

        public PayslipResponseBuilder month(Integer month) {
            this.month = month;
            return this;
        }

        public PayslipResponseBuilder year(Integer year) {
            this.year = year;
            return this;
        }

        public PayslipResponseBuilder baseSalary(Double baseSalary) {
            this.baseSalary = baseSalary;
            return this;
        }

        public PayslipResponseBuilder houseAmount(Double houseAmount) {
            this.houseAmount = houseAmount;
            return this;
        }

        public PayslipResponseBuilder transportAmount(Double transportAmount) {
            this.transportAmount = transportAmount;
            return this;
        }

        public PayslipResponseBuilder grossSalary(Double grossSalary) {
            this.grossSalary = grossSalary;
            return this;
        }

        public PayslipResponseBuilder employeeTax(Double employeeTax) {
            this.employeeTax = employeeTax;
            return this;
        }

        public PayslipResponseBuilder pension(Double pension) {
            this.pension = pension;
            return this;
        }

        public PayslipResponseBuilder medicalInsurance(Double medicalInsurance) {
            this.medicalInsurance = medicalInsurance;
            return this;
        }

        public PayslipResponseBuilder others(Double others) {
            this.others = others;
            return this;
        }

        public PayslipResponseBuilder netSalary(Double netSalary) {
            this.netSalary = netSalary;
            return this;
        }

        public PayslipResponseBuilder status(PayslipStatus status) {
            this.status = status;
            return this;
        }

        public PayslipResponseBuilder institution(String institution) {
            this.institution = institution;
            return this;
        }

        public PayslipResponseBuilder notificationMessage(String notificationMessage) {
            this.notificationMessage = notificationMessage;
            return this;
        }

        public PayslipResponse build() {
            return new PayslipResponse(id, employeeCode, employeeName, month, year, baseSalary,
                    houseAmount, transportAmount, grossSalary, employeeTax, pension,
                    medicalInsurance, others, netSalary, status, institution, notificationMessage);
        }
    }
}
