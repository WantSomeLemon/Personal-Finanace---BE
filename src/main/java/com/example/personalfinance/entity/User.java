package com.example.personalfinance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

/**
 * The 'User' class represents a user in the personal finance application.
 * It includes personal details such as name, email, password, and profile image.
 * It also inherits from 'BaseEntity', which includes fields like 'createdAt', 'updatedAt', and 'isDeleted'.
 */
@Table(name = "users") // Table name in the database
@Entity // Marks this class as a JPA entity
@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
public class User extends BaseEntity {

    /**
     * The unique identifier for each user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    /**
     * The first name of the user.
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * The last name of the user.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * The email address of the user. This is typically used for login and communication.
     */
    @Column(name = "email")
    private String email;

    /**
     * The password of the user. This is excluded from JSON serialization for security reasons.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Ensures the password is not included in the JSON response
    @Column(name = "password")
    private String password;

    /**
     * The profile image of the user, stored as a byte array. The maximum size is set to 1MB (1048576 bytes).
     */
    @Column(name = "profile_image", length = 1048576) // Maximum profile image size set to 1MB
    private byte[] profileImage;
}
