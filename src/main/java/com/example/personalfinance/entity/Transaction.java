package com.example.personalfinance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The 'Transaction' class represents a financial transaction for a user.
 * It includes details such as the transaction amount, description,
 * payment type, date and time of the transaction, the associated category,
 * account, and the user who made the transaction.
 * Implements Comparable<Transaction> for sorting transactions based on creation time.
 */
@Table(name = "transactions") // Table name in the database
@Entity // Marks this class as a JPA entity
@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Generates a no-argument constructor, required by JPA
public class Transaction extends BaseEntity implements Comparable<Transaction> {

    /**
     * The unique identifier for each transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The amount of money involved in the transaction (positive for income, negative for expenses).
     */
    @Column(name = "amount")
    private double amount;

    /**
     * A description of the transaction, providing more context (e.g., "Grocery shopping", "Salary payment").
     */
    @Column(name = "description")
    private String description;

    /**
     * The payment type used for the transaction (e.g., "Cash", "Credit", "Debit").
     */
    @Column(name = "payment_type")
    private String paymentType;

    /**
     * The date and time when the transaction took place.
     * Represented as a Long value (timestamp).
     */
    @Column(name = "date_time")
    private Long dateTime;

    /**
     * The category associated with the transaction (e.g., "Food", "Salary").
     * This is a relationship to the 'Category' entity.
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * The account associated with the transaction (e.g., "Checking Account").
     * This is a relationship to the 'Account' entity.
     */
    @ManyToOne
    @JoinColumn() // Foreign key is automatically inferred by JPA for 'account' field
    private Account account;

    /**
     * The user who made the transaction.
     * This is a relationship to the 'User' entity.
     * The user is excluded from the serialized JSON response.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Ensures user information is not included in the JSON response
    private User user;

    /**
     * Constructor to initialize a new Transaction.
     *
     * @param amount The amount of the transaction
     * @param description A description of the transaction
     * @param paymentType The payment method used
     * @param dateTime The timestamp of when the transaction occurred
     * @param category The category for the transaction
     * @param account The account associated with the transaction
     * @param user The user making the transaction
     */
    public Transaction(double amount, String description, String paymentType, Long dateTime, Category category, Account account, User user) {
        this.amount = amount;
        this.description = description;
        this.paymentType = paymentType;
        this.dateTime = dateTime;
        this.category = category;
        this.account = account;
        this.user = user;
    }
    
    /**
     * Compares two transactions based on their creation timestamp.
     * This is useful for sorting transactions by their creation date.
     *
     * @param other The other transaction to compare to
     * @return A negative integer, zero, or a positive integer as this transaction's 
     *         creation time is less than, equal to, or greater than the specified transaction's creation time
     */
    @Override
    public int compareTo(Transaction other) {
        return this.createdAt.compareTo(other.getCreatedAt());
    }
}
