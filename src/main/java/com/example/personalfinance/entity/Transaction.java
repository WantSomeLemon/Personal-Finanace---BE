package com.example.personalfinance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Table(name="transactions")
@Entity
@Data
public class Transaction extends BaseEntity implements Comparable<Transaction>   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "amount", nullable = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Amount must be non-negative.")
    private double amount;

    @Column(name = "description", nullable = false)
    @NotNull(message = "Description is required.")
    @Size(max = 200, message = "Description must be at most 200 characters.")
    private String description;
    
    @Column(name = "payment_type", nullable = false)
    @NotNull(message = "Payment type is required.")
    private String paymentType;

    @Column(name = "date_time", nullable = false)
    @NotNull(message = "Date and time are required.")
    private Long dateTime;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "Category is required.")
    private Category category;

    @ManyToOne
    @JoinColumn() //Quên không đặt tên nên giờ hệ thống tự mapping thành account_account_id
    @NotNull(message = "Account is required.")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User is required.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    public Transaction(double amount, String description, String paymentType, Long dateTime, Category category, Account account, User user) {
        this.amount = amount;
        this.description = description;
        this.paymentType = paymentType;
        this.dateTime = dateTime;
        this.category = category;
        this.account = account;
        this.user = user;
    }

    public Transaction() {

    }

    @Override
    public int compareTo(Transaction other) {
        return this.createdAt.compareTo(other.getCreatedAt());
    }
}
