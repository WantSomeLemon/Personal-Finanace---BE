package com.example.personalfinance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

/**
 * The 'Category' class represents a financial category that can be used to 
 * classify transactions in the system. It includes the category's details such as 
 * its name, type, and description, and is linked to a specific user.
 */
@Table(name = "categories") // Table name in the database
@Entity // Marks this class as a JPA entity
@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
public class Category extends BaseEntity {

    /**
     * The unique identifier for each category in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    /**
     * The name of the category (e.g., "Food", "Transport", etc.).
     */
    @Column(name = "category_name")
    private String name;

    /**
     * The type of the category (e.g., "Expense", "Income").
     */
    @Column(name = "category_type")
    private String type;

    /**
     * A description of the category, providing additional context (e.g., "Food purchases for daily needs").
     */
    @Column(name = "category_description")
    private String description;

    /**
     * The user associated with this category. A category belongs to a single user.
     * This is used for filtering categories by user.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Prevents the user from being serialized in responses
    private User userId;

}
