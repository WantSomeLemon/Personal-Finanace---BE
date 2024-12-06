package com.example.personalfinance.exception.debts;

public class DebtNotFoundException extends RuntimeException {
    public DebtNotFoundException(String message) {
        super(message);
    }
}
