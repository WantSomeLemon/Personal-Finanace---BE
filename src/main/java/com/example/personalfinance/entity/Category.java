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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Table(name="categories")
@Entity
@Data
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Column(name = "name", nullable = false)
    @NotNull(message = "Category name is required.")
    @Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters.")
    private String name;

    @Column(name = "type", nullable = false)
    @NotNull(message = "Category type is required.")
    private String type;

    @Column(name = "description")
    @Size(max = 200, message = "Description must be at most 200 characters.")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User is required.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User userId;


}
