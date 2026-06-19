package com.gov.rw.erp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Message entity — stores the salary credit notification sent to an employee
 * when the admin approves payroll for a given month/year.
 *
 * Relationship: Many messages can belong to one employee (ManyToOne).
 * Format: "Dear <FIRSTNAME> Your salary of MM/YYYY from <INSTITUTION> <AMOUNT> FRW
 *          has been credited to your <EMPLOYEE_CODE> account Successfully."
 */
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The employee this message was sent to.
     * ManyToOne: one employee can receive many messages (one per payroll approval).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * The full notification text of the message.
     * Named 'text' to match entity design specification.
     */
    @Column(nullable = false, length = 1000)
    private String text;

    /** Payroll month this message relates to (1-12) */
    @Column(nullable = false)
    private Integer month;

    /** Payroll year this message relates to */
    @Column(nullable = false)
    private Integer year;

    /** Timestamp when the message was generated and persisted */
    @Column(nullable = false)
    private LocalDateTime dateTime;

    public Message() {}

    public Message(Long id, Employee employee, String text, Integer month, Integer year, LocalDateTime dateTime) {
        this.id = id;
        this.employee = employee;
        this.text = text;
        this.month = month;
        this.year = year;
        this.dateTime = dateTime;
    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    // ---- Builder ----

    public static class MessageBuilder {
        private Long id;
        private Employee employee;
        private String text;
        private Integer month;
        private Integer year;
        private LocalDateTime dateTime;

        public MessageBuilder id(Long id) { this.id = id; return this; }
        public MessageBuilder employee(Employee employee) { this.employee = employee; return this; }
        public MessageBuilder text(String text) { this.text = text; return this; }
        public MessageBuilder month(Integer month) { this.month = month; return this; }
        public MessageBuilder year(Integer year) { this.year = year; return this; }
        public MessageBuilder dateTime(LocalDateTime dateTime) { this.dateTime = dateTime; return this; }

        public Message build() {
            return new Message(id, employee, text, month, year, dateTime);
        }
    }
}
