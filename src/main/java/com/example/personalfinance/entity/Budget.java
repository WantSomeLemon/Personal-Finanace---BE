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
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "budgets")
@Entity
@Data
@NoArgsConstructor
public class Budget extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "used")
    @DecimalMin(value = "0.0", inclusive = true, message = "Used amount must be non-negative.")
    private double used = 0.0;

    @Column(name = "balance")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be non-negative.")
    private double balance = 0.0;

    @Column(name = "amount", nullable = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Amount must be non-negative.")
    private double amount;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "Category is required.")
    private Category category;
    

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User is required.")
    private User user;

    // Custom constructor that excludes 'budgetId' because it is auto-generated
    public Budget(Category category, double amount, User user, Long used, Long balance) {
        this.category = category;
        this.amount = amount;
        this.user = user;
        this.used = used;
        this.balance = balance;
    }

    public Budget(Category category, double amount, User user) {
        this.category = category;
        this.amount = amount;
        this.user = user;
    }
}
