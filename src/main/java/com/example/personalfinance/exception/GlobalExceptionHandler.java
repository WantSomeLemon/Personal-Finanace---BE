package com.example.personalfinance.exception;

import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.exception.account.AccountNotFoundException;
import com.example.personalfinance.exception.account.AccountUpdateException;
import com.example.personalfinance.exception.account.TransactionProcessingException;
import com.example.personalfinance.exception.budget.*;
import com.example.personalfinance.exception.categories.CategoryAlreadyExistsException;
import com.example.personalfinance.exception.categories.CategoryDeleteFailedException;
import com.example.personalfinance.exception.categories.CategoryNotFoundException;
import com.example.personalfinance.exception.transaction.TransactionNotFoundException;
import com.example.personalfinance.exception.user.InvalidLoginException;
import com.example.personalfinance.exception.user.ProfileImageUpdateException;
import com.example.personalfinance.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //Account
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Account Not Found", ex.getMessage());
    }


    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Unauthorized Access", ex.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid Input", ex.getMessage());
    }

    @ExceptionHandler(JWTException.class)
    public ResponseEntity<ErrorResponse> handleJWTException(JWTException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "JWT Error", ex.getMessage());
    }

    @ExceptionHandler(TransactionProcessingException.class)
    public ResponseEntity<ErrorResponse> handleTransactionProcessingException(TransactionProcessingException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction Error", ex.getMessage());
    }

    @ExceptionHandler(AccountUpdateException.class)
    public ResponseEntity<ErrorResponse> handleAccountUpdateException(AccountUpdateException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Account Update Failed", ex.getMessage());
    }

    //Budget
    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBudgetNotFoundException(BudgetNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Budget Not Found", ex.getMessage());
    }

    @ExceptionHandler(BudgetAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleBudgetAlreadyExistsException(BudgetAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Budget Conflict", ex.getMessage());
    }

    @ExceptionHandler(BudgetCreationException.class)
    public ResponseEntity<ErrorResponse> handleBudgetCreationException(BudgetCreationException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Budget Creation Failed", ex.getMessage());
    }

    @ExceptionHandler(BudgetUpdateException.class)
    public ResponseEntity<ErrorResponse> handleBudgetUpdateException(BudgetUpdateException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Budget Update Failed", ex.getMessage());
    }

    @ExceptionHandler(BudgetDeletionException.class)
    public ResponseEntity<ErrorResponse> handleBudgetDeletionException(BudgetDeletionException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Budget Deletion Failed", ex.getMessage());
    }

    @ExceptionHandler(BudgetFetchException.class)
    public ResponseEntity<BaseResponse> handleBudgetFetchException(BudgetFetchException ex) {
        return new ResponseEntity<>(new BaseResponse("error", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    //Category
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Category Not Found", ex.getMessage());
    }
    
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Category Already Exists", ex.getMessage());
    }
    
    @ExceptionHandler(CategoryDeleteFailedException.class)
    public ResponseEntity<ErrorResponse> handleCategoryDeleteFailedException(CategoryDeleteFailedException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Category Delete Failed", ex.getMessage());
    }
    
    //User
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "User Not Found", ex.getMessage());
    }
    
    @ExceptionHandler(ProfileImageUpdateException.class)
    public ResponseEntity<BaseResponse> handleProfileImageUpdateException(ProfileImageUpdateException ex) {
        return new ResponseEntity<>(new BaseResponse("error", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<BaseResponse> handleInvalidLoginException(InvalidLoginException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse(ex.getMessage(), null));
    }
    
    //Transaction
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Transaction Not Found", ex.getMessage());
    }
    
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error, String message) {
        return new ResponseEntity<>(new ErrorResponse(error, message), status);
    }

    public static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }
    }
}
