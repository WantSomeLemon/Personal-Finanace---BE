package com.example.personalfinance.bean.request;

import lombok.Data;

/**
 * The 'LoginRequest' class is used to capture the data required for a user to log in.
 * It contains the necessary fields for the user's email and password.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods automatically
public class LoginRequest {

  /**
   * The email of the user trying to log in.
   * This will be used to identify the user in the system.
   */
  private String email;

  /**
   * The password associated with the user's account.
   * This will be used to authenticate the user and grant access to the application.
   */
  private String password;
}
