package com.example.personalfinance.exception;

public class OperationFailedException extends RuntimeException {
  public OperationFailedException(String message) {
    super(message);
  }
}
