package com.example.personalfinance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

/**
 * The 'Goals' class represents a financial goal for a user.
 * It includes details such as the name of the goal, its description,
 * the target amount to be saved, the goal's status, and the target date.
 * It is associated with a specific user.
 */
@Table(name = "goals") // Table name in the database
@Entity // Marks this class as a JPA entity
@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
public class Goals extends BaseEntity {

    /**
     * The unique identifier for each goal.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the financial goal (e.g., "Vacation Fund", "Emergency Fund").
     */
    @Column(name = "goal_name")
    private String name;

    /**
     * A description of the goal, providing more context (e.g., "Saving for a vacation in 2025").
     */
    @Column(name = "goal_description")
    private String description;

    /**
     * The target amount of money needed to achieve the goal.
     */
    @Column(name = "goal_amount")
    private double targetAmount;

    /**
     * The current status of the goal (e.g., "Ongoing", "Achieved", "Pending").
     */
    @Column(name = "goal_status")
    private String Status;

    /**
     * The target date by which the goal should be achieved (e.g., "2025-12-31").
     * Represented as a string to keep it simple, it could also be a date type if needed.
     */
    @Column(name = "goal_targetdate")
    private String targetDate;

    /**
     * The user associated with this goal. A goal belongs to a single user.
     * This field creates a relationship between the 'Goals' entity and the 'User' entity.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Ensures user information is not included in the JSON response
    private User user;
}
