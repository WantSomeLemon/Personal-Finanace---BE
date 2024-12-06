package com.example.personalfinance.exception;

public class JWTException extends RuntimeException {
    public JWTException(String message) {
        super(message);
    }
}
