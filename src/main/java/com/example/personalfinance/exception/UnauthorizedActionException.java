package com.example.personalfinance.exception;

public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
