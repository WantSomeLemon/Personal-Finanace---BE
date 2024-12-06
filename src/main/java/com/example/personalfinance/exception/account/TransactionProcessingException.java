package com.example.personalfinance.exception.account;

public class TransactionProcessingException extends RuntimeException {
    public TransactionProcessingException(String message) {
        super(message);
    }
}
