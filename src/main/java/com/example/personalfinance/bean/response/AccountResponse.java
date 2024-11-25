package com.example.personalfinance.bean.response;

import lombok.Data;

import java.util.List;

/**
 * Represents a response object for account-related information.
 * Contains details such as account ID, name, current balance, 
 * payment types, total expense, and total income.
 */

@Data
public class AccountResponse {
    private int accountId; // Unique identifier for the account
    private String name; // Name of the account
    private double currentBalance; // Current balance of the account
    private String paymentTypes; // Comma-separated string of payment types associated with the account
    private double totalExpense; // Total expenses associated with the account
    private double totalIncome; // Total income associated with the account

    /**
     * Constructs an AccountResponse object.
     *
     * @param accountId     the unique identifier for the account
     * @param name          the name of the account
     * @param currentBalance the current balance of the account
     * @param paymentTypes  the list of payment types associated with the account
     * @param totalExpense  the total expenses associated with the account
     * @param totalIncome   the total income associated with the account
     */
    public AccountResponse(int accountId, String name, double currentBalance, List<String> paymentTypes, double totalExpense, double totalIncome) {
        this.accountId = accountId;
        this.name = name;
        this.currentBalance = currentBalance;
        // Converts the list of payment types into a comma-separated string
        this.paymentTypes = String.join(", ", paymentTypes);
        this.totalExpense = totalExpense;
        this.totalIncome = totalIncome;
    }
}










