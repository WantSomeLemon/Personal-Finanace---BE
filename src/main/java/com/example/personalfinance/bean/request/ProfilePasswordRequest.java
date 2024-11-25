package com.example.personalfinance.bean.request;

import lombok.Data;

/**
 * The 'ProfilePasswordRequest' class is used to capture the data required for updating a user's password.
 * It includes both the old and new passwords.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods automatically
public class ProfilePasswordRequest {

    /**
     * The new password that the user wants to set for their profile.
     */
    private String newPassword;

    /**
     * The current password that the user is providing to authenticate the change.
     */
    private String oldPassword;
}
