package com.example.personalfinance.bean.request;

import lombok.Data;

/**
 * The 'TransactionRequest' class is used to capture the details of a transaction 
 * when a user is creating or updating a transaction.
 */
@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
public class TransactionRequest {

    /**
     * The amount of money involved in the transaction.
     */
    private double amount;

    /**
     * A brief description of the transaction, e.g., purchase details or payment.
     */
    private String description;

    /**
     * The type of payment used in the transaction (e.g., credit card, cash, etc.).
     */
    private String paymentType;

    /**
     * The ID of the category associated with the transaction (e.g., food, utilities, etc.).
     */
    private Integer categoryId;

    /**
     * The ID of the account that the transaction is linked to.
     */
    private Integer accountId;

    /**
     * The timestamp of when the transaction occurred, stored as a Unix epoch time.
     */
    private Long dateTime;
}
