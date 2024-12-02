package com.example.personalfinance.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.personalfinance.bean.response.BaseResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<BaseResponse> handleDuplicateException(DuplicateException ex) {
        return ResponseEntity.badRequest().body(new BaseResponse("error", ex.getMessage()));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<BaseResponse> handleAccountNotFound(AccountNotFoundException ex) {
        return ResponseEntity.badRequest().body(new BaseResponse("error", ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<BaseResponse> handleUnauthorizedAction(UnauthorizedActionException ex) {
        return ResponseEntity.status(403).body(new BaseResponse("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleGeneralException(Exception ex) {
        return ResponseEntity.internalServerError().body(new BaseResponse("error", "An unexpected error occurred."));
    }
}
