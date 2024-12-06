package com.example.personalfinance.entity;

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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Table(name = "goals")
@Entity
@Data
public class Goals extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goal_name", nullable = false)
    @NotNull(message = "Goal name is required.")
    @Size(min = 3, max = 100, message = "Goal name must be between 3 and 100 characters.")
    private String name;
    
    @Column(name = "goal_description")
    @Size(max = 500, message = "Description must be at most 500 characters.")
    private String description;

    @Column(name = "goal_amount", nullable = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Target amount must be non-negative.")
    private double targetAmount;

    @Column(name = "goal_status", nullable = false)
    @NotNull(message = "Status is required.")
    @Pattern(regexp = "Active|Completed|In Progress", message = "Status must be Active, Completed, or In Progress.")
    private String status;

    @Column(name = "goal_targetdate", nullable = false)
    @NotNull(message = "Target date is required.")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Target date must follow the format YYYY-MM-DD.")
    private String targetDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User is required.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
}
