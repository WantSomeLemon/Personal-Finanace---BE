package com.example.personalfinance.exception.categories;

public class CategoryDeleteFailedException extends RuntimeException {
    public CategoryDeleteFailedException(String message) {
        super(message);
    }
}
