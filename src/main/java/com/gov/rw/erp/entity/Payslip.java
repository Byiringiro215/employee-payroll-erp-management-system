package com.gov.rw.erp.entity;

import com.gov.rw.erp.enums.PayslipStatus;
import jakarta.persistence.*;

/**
 * Payslip entity — represents a single monthly salary record for one employee.
 *
 * Relationships:
 *   - ManyToOne with Employee: one employee has many payslips (one per month/year)
 *
 * Salary formula:
 *   grossSalary  = baseSalary + houseAmount (14%) + transportAmount (14%)
 *   netSalary    = baseSalary - (employeeTax + pension + medicalInsurance + others)
 *
 * Unique constraint on (employee_id, month, year) prevents duplicate payroll per period.
 */
@Entity
@Table(
    name = "payslips",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_payslip_employee_month_year",
        columnNames = {"employee_id", "month", "year"}
    )
)
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The employee this payslip belongs to.
     * ManyToOne: one employee can have many payslips across different months.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /** Payroll month (1 = January, 12 = December) */
    @Column(nullable = false)
    private Integer month;

    /** Payroll year (e.g. 2025) */
    @Column(nullable = false)
    private Integer year;

    /** Base salary from Employment record — the foundation of all calculations */
    @Column(nullable = false)
    private Double baseSalary;

    // ---- Allowances (added to base to produce gross) ----

    /** House allowance = baseSalary × 14% */
    @Column(nullable = false)
    private Double houseAmount;

    /** Transport allowance = baseSalary × 14% */
    @Column(nullable = false)
    private Double transportAmount;

    /** Gross salary = baseSalary + houseAmount + transportAmount */
    @Column(nullable = false)
    private Double grossSalary;

    // ---- Deductions (subtracted from base to produce net) ----

    /** Employee income tax = baseSalary × 30% */
    @Column(nullable = false)
    private Double employeeTax;

    /** Pension contribution = baseSalary × 6% */
    @Column(nullable = false)
    private Double pension;

    /** Medical insurance = baseSalary × 5% */
    @Column(nullable = false)
    private Double medicalInsurance;

    /** Other deductions = baseSalary × 5% */
    @Column(nullable = false)
    private Double others;

    /** Net salary = baseSalary - (employeeTax + pension + medicalInsurance + others) */
    @Column(nullable = false)
    private Double netSalary;

    /**
     * Payslip status lifecycle: PENDING → PAID
     * Status is set to PENDING on generation and PAID on payroll approval.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayslipStatus status;

    public Payslip() {}

    public Payslip(Long id, Employee employee, Integer month, Integer year,
                   Double baseSalary, Double houseAmount, Double transportAmount,
                   Double grossSalary, Double employeeTax, Double pension,
                   Double medicalInsurance, Double others, Double netSalary,
                   PayslipStatus status) {
        this.id = id;
        this.employee = employee;
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
    }

    public static PayslipBuilder builder() {
        return new PayslipBuilder();
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(Double baseSalary) { this.baseSalary = baseSalary; }

    public Double getHouseAmount() { return houseAmount; }
    public void setHouseAmount(Double houseAmount) { this.houseAmount = houseAmount; }

    public Double getTransportAmount() { return transportAmount; }
    public void setTransportAmount(Double transportAmount) { this.transportAmount = transportAmount; }

    public Double getGrossSalary() { return grossSalary; }
    public void setGrossSalary(Double grossSalary) { this.grossSalary = grossSalary; }

    public Double getEmployeeTax() { return employeeTax; }
    public void setEmployeeTax(Double employeeTax) { this.employeeTax = employeeTax; }

    public Double getPension() { return pension; }
    public void setPension(Double pension) { this.pension = pension; }

    public Double getMedicalInsurance() { return medicalInsurance; }
    public void setMedicalInsurance(Double medicalInsurance) { this.medicalInsurance = medicalInsurance; }

    public Double getOthers() { return others; }
    public void setOthers(Double others) { this.others = others; }

    public Double getNetSalary() { return netSalary; }
    public void setNetSalary(Double netSalary) { this.netSalary = netSalary; }

    public PayslipStatus getStatus() { return status; }
    public void setStatus(PayslipStatus status) { this.status = status; }

    // ---- Builder ----

    public static class PayslipBuilder {
        private Long id;
        private Employee employee;
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

        public PayslipBuilder id(Long id) { this.id = id; return this; }
        public PayslipBuilder employee(Employee employee) { this.employee = employee; return this; }
        public PayslipBuilder month(Integer month) { this.month = month; return this; }
        public PayslipBuilder year(Integer year) { this.year = year; return this; }
        public PayslipBuilder baseSalary(Double baseSalary) { this.baseSalary = baseSalary; return this; }
        public PayslipBuilder houseAmount(Double houseAmount) { this.houseAmount = houseAmount; return this; }
        public PayslipBuilder transportAmount(Double transportAmount) { this.transportAmount = transportAmount; return this; }
        public PayslipBuilder grossSalary(Double grossSalary) { this.grossSalary = grossSalary; return this; }
        public PayslipBuilder employeeTax(Double employeeTax) { this.employeeTax = employeeTax; return this; }
        public PayslipBuilder pension(Double pension) { this.pension = pension; return this; }
        public PayslipBuilder medicalInsurance(Double medicalInsurance) { this.medicalInsurance = medicalInsurance; return this; }
        public PayslipBuilder others(Double others) { this.others = others; return this; }
        public PayslipBuilder netSalary(Double netSalary) { this.netSalary = netSalary; return this; }
        public PayslipBuilder status(PayslipStatus status) { this.status = status; return this; }

        public Payslip build() {
            return new Payslip(id, employee, month, year, baseSalary, houseAmount,
                    transportAmount, grossSalary, employeeTax, pension,
                    medicalInsurance, others, netSalary, status);
        }
    }
}
