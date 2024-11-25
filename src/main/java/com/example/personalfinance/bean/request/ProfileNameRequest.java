package com.example.personalfinance.bean.request;

import lombok.Data;

/**
 * The 'ProfileNameRequest' class is used to capture the data required for updating a user's profile name.
 * It contains fields for the user's first and last name.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods automatically
public class ProfileNameRequest {

    /**
     * The first name of the user that they want to set as part of their profile.
     */
    private String firstName;

    /**
     * The last name of the user that they want to set as part of their profile.
     */
    private String lastName;
}
