package com.example.personalfinance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class BaseEntity {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "deleted", columnDefinition = "Bit(1) default false")
    private boolean isDeleted = false;
    
    @Column(updatable = false)
    @CreationTimestamp
    public LocalDateTime createdAt;
    
    @Column(updatable = true)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
