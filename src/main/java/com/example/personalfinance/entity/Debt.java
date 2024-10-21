package com.example.personalfinance.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "debts")
@Entity
@Data
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer debtId;

    @Column(name = "debt_amount")
    private double amount;
    @Column(name = "debt_duedate")
    private String dueDate;
    @Column(name = "debt_moneyfrom")
    private String moneyFrom;
    @Column(name = "debt_status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "main_user")
    private User user;
}
