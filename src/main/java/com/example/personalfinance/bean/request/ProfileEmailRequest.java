package com.example.personalfinance.bean.request;

import lombok.Data;

/**
 * The 'ProfileEmailRequest' class is used to capture the data required for updating a user's email address.
 * It contains a single field for the new email to be set.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods automatically
public class ProfileEmailRequest {

    /**
     * The new email address that the user wants to set.
     * This field will be used when updating the user's email profile information.
     */
    private String email;
}
