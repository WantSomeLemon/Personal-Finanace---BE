package com.example.personalfinance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * This is a base entity class that provides common fields for all entities.
 * It includes fields for soft deletion, creation timestamp, and update timestamp.
 * All entities that need these fields should extend this class.
 */
@MappedSuperclass // Indicates that this class is a base class for JPA entities, not a standalone entity itself
@Data // Generates getters, setters, equals, hashCode, and toString methods
public abstract class BaseEntity {

    /**
     * The 'isDeleted' field marks the entity as deleted without actually removing it from the database.
     * This is used for soft delete functionality.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Prevents this field from being serialized in JSON responses
    @Column(name = "deleted", columnDefinition = "Bit(1) default false") // Maps to the 'deleted' column in the database, with a default value of false
    private boolean isDeleted = false;

    /**
     * The 'createdAt' field is automatically set when the entity is created.
     * It stores the timestamp when the entity is first persisted.
     */
    @Column(updatable = false, nullable = false) // Ensures the field is not updated and is always nullable
    @CreationTimestamp // Automatically sets this field with the current timestamp when the entity is created
    public LocalDateTime createdAt;

    /**
     * The 'updatedAt' field is automatically updated whenever the entity is modified.
     * It stores the timestamp of the last update.
     */
    @Column(updatable = true, nullable = false) // Ensures this field is updated when the entity is updated
    @UpdateTimestamp // Automatically updates this field with the current timestamp whenever the entity is updated
    public LocalDateTime updatedAt;
}
