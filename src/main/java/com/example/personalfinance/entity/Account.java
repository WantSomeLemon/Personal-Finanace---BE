package com.example.personalfinance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Table(name = "accounts")  // Specifies the name of the table in the database
@Entity  // Marks this class as an entity to be mapped to a database table
@Data  // Generates getters, setters, equals, hashcode, and toString methods
public class Account extends BaseEntity {
    @Id  // Specifies the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generates the primary key value
    private int accountId;

    @Column(name = "name")  // Maps this field to the "name" column in the table
    private String name;

    @Column(name = "current_balance")  // Maps this field to the "current_balance" column in the table
    private double currentBalance;

    @Column(name = "payment_types")  // Maps this field to the "payment_types" column in the table
    private String paymentTypes;

    @ManyToOne  // Establishes a many-to-one relationship with the User entity
    @JoinColumn(name = "user_id")  // Specifies the foreign key column in this entity
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // Prevents this field from being serialized into JSON when retrieved
    private User user;

    // Converts the paymentTypes (stored as a comma-separated string) to a List
    public List<String> getPaymentTypes() {
        return Arrays.asList(paymentTypes.split(", "));  // Splits the string and returns as a List of Strings
    }

    // Converts the List of payment types to a comma-separated string before storing it in the database
    public void setPaymentTypes(List<String> paymentTypes) {
        this.paymentTypes = String.join(", ", paymentTypes);  // Joins the List elements into a comma-separated string
    }
}