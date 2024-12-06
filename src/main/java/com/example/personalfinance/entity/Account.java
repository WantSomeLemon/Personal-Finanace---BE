package com.example.personalfinance.entity;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Table(name = "accounts")
@Entity
@Data
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    @Column(name = "name", nullable = false)
    @NotNull(message = "Account name is required.")
    @Size(min = 3, max = 50, message = "Account name must be between 3 and 50 characters.")
    private String name;

    @Column(name = "current_balance")
    @DecimalMin(value = "0.0", inclusive = true, message = "Current balance must be non-negative.")
    private double currentBalance;

    @Column(name = "payment_types", nullable = false)
    @NotNull(message = "Payment types are required.")
    private String paymentTypes;
    
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User is required.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    public List<String> getPaymentTypes() {
        return Arrays.asList(paymentTypes.split(", "));
    }

    public void setPaymentTypes(List<String> paymentTypes) {
        this.paymentTypes = String.join(", ", paymentTypes);
    }
}
