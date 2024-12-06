package com.example.personalfinance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Table(name = "debts")
@Entity
@Data
public class Debt extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer debtId;

    @Column(name = "debt_amount", nullable = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Debt amount must be non-negative.")
    private double amount;

    @Column(name = "debt_duedate", nullable = false)
    @NotNull(message = "Due date is required.")
    private String dueDate;

    @Column(name = "debt_moneyfrom", nullable = false)
    @NotNull(message = "Money source is required.")
    @Size(max = 100, message = "Money source must be at most 100 characters.")
    private String moneyFrom;

    @Column(name = "debt_status", nullable = false)
    @NotNull(message = "Status is required.")
    private String status;

    @ManyToOne
    @JoinColumn(name = "main_user")
    @NotNull(message = "User is required.")
    private User user;
}
