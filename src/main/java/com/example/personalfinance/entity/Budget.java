package com.example.personalfinance.entity;

import jakarta.persistence.*;
import lombok.Data;
@Table(name = "budgets")
@Entity
@Data
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    @Column(name = "budget_status")
    private Long used;

    @Column(name = "budget_balance")
    private Long balance;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}
