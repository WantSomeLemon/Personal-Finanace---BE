package com.example.personalfinance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "goals")
@Entity
@Data
public class Goals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goal_name")
    private String name;
    @Column(name = "goal_description")
    private String description;
    @Column(name = "goal_amount")
    private double targetAmount;
    @Column(name = "goal_status")
    private String Status;
    @Column(name = "goal_targetdate")
    private int targetDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
}
