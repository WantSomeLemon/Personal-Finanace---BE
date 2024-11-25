package com.example.personalfinance.bean.request;

import lombok.Data;

/**
 * The 'RegisterRequest' class is used to capture the data required for registering a new user.
 * It includes the user's email, password, first name, and last name.
 */
@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
public class RegisterRequest {

    /**
     * The email address of the user, which will be used for login and notifications.
     */
    private String email;

    /**
     * The password chosen by the user for account authentication.
     */
    private String password;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;
}
