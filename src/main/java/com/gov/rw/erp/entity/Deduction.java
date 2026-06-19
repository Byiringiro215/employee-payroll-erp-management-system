package com.gov.rw.erp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "deductions")
public class Deduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String deductionName;

    // Percentage value e.g. 30 for 30%
    @Column(nullable = false)
    private Double percentage;

    public Deduction() {}

    public Deduction(Long id, String deductionName, Double percentage) {
        this.id = id;
        this.deductionName = deductionName;
        this.percentage = percentage;
    }

    public static DeductionBuilder builder() {
        return new DeductionBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeductionName() {
        return deductionName;
    }

    public void setDeductionName(String deductionName) {
        this.deductionName = deductionName;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public static class DeductionBuilder {
        private Long id;
        private String deductionName;
        private Double percentage;

        public DeductionBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DeductionBuilder deductionName(String deductionName) {
            this.deductionName = deductionName;
            return this;
        }

        public DeductionBuilder percentage(Double percentage) {
            this.percentage = percentage;
            return this;
        }

        public Deduction build() {
            return new Deduction(id, deductionName, percentage);
        }
    }
}
