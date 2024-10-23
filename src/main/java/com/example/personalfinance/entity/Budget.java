package com.example.personalfinance.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "budgets")
@Entity
@Data
@NoArgsConstructor
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

    // Custom constructor that excludes 'budgetId' because it is auto-generated
    public Budget(Category category, double amount, User user, Long used, Long balance) {
        this.category = category;
        this.amount = amount;
        this.user = user;
        this.used = used;
        this.balance = balance;
    }

}
