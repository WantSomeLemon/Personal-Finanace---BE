package com.example.personalfinance.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The 'Budget' entity represents a financial budget for a specific user within a category.
 * It extends the 'BaseEntity' class to include audit fields like createdAt and updatedAt.
 */
@Table(name = "budgets") // Specifies the name of the table in the database
@Entity // Marks this class as a JPA entity to be mapped to a database table
@Data // Generates getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Generates a no-argument constructor, required by JPA
public class Budget extends BaseEntity {

    /**
     * The unique identifier for each budget entry.
     * This will be automatically generated when the budget is created.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID using the identity strategy
    private Long id;

    /**
     * The amount of the budget that has been used.
     * This starts at 0 and can be updated as expenses are recorded.
     */
    @Column(name = "budget_status") // Maps to the 'budget_status' column in the database
    private double used = 0.0;

    /**
     * The total balance of the budget.
     * This is the initial budget amount and can be used to calculate remaining balance.
     */
    @Column(name = "budget_balance") // Maps to the 'budget_balance' column in the database
    private double balance = 0.0;

    /**
     * The category to which this budget is assigned.
     * A many-to-one relationship where multiple budgets can belong to a single category.
     */
    @ManyToOne
    @JoinColumn(name = "category_id") // Specifies the foreign key column that references the 'Category' entity
    private Category category;

    /**
     * The amount set for this budget.
     * This represents the total available amount in the budget for the given category.
     */
    private double amount;

    /**
     * The user associated with this budget.
     * A many-to-one relationship where multiple budgets can belong to a single user.
     */
    @ManyToOne
    @JoinColumn(name = "user_id") // Specifies the foreign key column that references the 'User' entity
    private User user;

    /**
     * Custom constructor to create a budget with a specific category, amount, and user.
     * This allows the creation of a budget without needing to set fields manually.
     *
     * @param category The category to which the budget belongs.
     * @param amount   The amount set for the budget.
     * @param user     The user associated with the budget.
     */
    public Budget(Category category, double amount, User user) {
        this.category = category;
        this.amount = amount;
        this.user = user;
    }
}