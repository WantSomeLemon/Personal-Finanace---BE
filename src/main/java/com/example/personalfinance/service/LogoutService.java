package com.example.personalfinance.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * Service that handles the logout functionality for the application.
 * Implements the LogoutHandler interface to provide custom logout logic.
 */
@Service
public class LogoutService implements LogoutHandler {

  /**
   * This method is triggered when a user logs out.
   * It is responsible for handling custom logout logic, such as invalidating sessions or clearing tokens.
   *
   * @param request the HttpServletRequest containing information about the request made by the user
   * @param response the HttpServletResponse used to send a response back to the user
   * @param authentication the Authentication object that holds the user's authentication details
   */
  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    // Retrieve the Authorization header (typically used for the token in request)
    String authHeader = request.getHeader("Authorization");

    // Logic to handle the token invalidation or logout operation could go here
    // Currently, it's just retrieving the Authorization header for further processing.
  }
}
