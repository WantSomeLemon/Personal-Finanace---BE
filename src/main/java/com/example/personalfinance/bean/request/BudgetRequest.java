package com.example.personalfinance.bean.request;

import lombok.Data;

/**
 * The 'BudgetRequest' class is used to capture the data from a user when creating or updating a budget.
 * It contains the necessary fields to specify the budget's category and the amount.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods automatically
public class BudgetRequest {

    /**
     * The ID of the category to which the budget will be assigned.
     * This will be used to associate the budget with an existing category.
     */
    private int categoryId;

    /**
     * The amount set for the budget.
     * This represents the total available budget for the given category.
     */
    private double amount;
}
