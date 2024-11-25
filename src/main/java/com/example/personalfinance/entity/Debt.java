package com.example.personalfinance.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * The 'Debt' class represents a financial debt record for a user.
 * It includes details such as the amount of debt, the due date, 
 * the source of the money, and the current status of the debt.
 * It is associated with a specific user.
 */
@Table(name = "debts") // Table name in the database
@Entity // Marks this class as a JPA entity
@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
public class Debt extends BaseEntity {

    /**
     * The unique identifier for each debt record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer debtId;

    /**
     * The amount of debt owed by the user.
     */
    @Column(name = "debt_amount")
    private double amount;

    /**
     * The due date for the debt repayment.
     * Represented as a string, it could be formatted as a date string (e.g., "2024-12-31").
     */
    @Column(name = "debt_duedate")
    private String dueDate;

    /**
     * The entity or person from whom the money is owed (e.g., "Bank", "Friend").
     */
    @Column(name = "debt_moneyfrom")
    private String moneyFrom;

    /**
     * The current status of the debt (e.g., "Pending", "Paid", "Overdue").
     */
    @Column(name = "debt_status")
    private String status;

    /**
     * The user associated with this debt. A debt belongs to a single user.
     * This is used for linking a debt to the user who owes the money.
     */
    @ManyToOne
    @JoinColumn(name = "main_user")
    private User user;
}
