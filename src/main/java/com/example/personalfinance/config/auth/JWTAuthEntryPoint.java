package com.example.personalfinance.config.auth;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles unauthorized access to protected resources.
 * This entry point is triggered whenever an unauthenticated user 
 * tries to access a secured endpoint in the application.
 */

@Component
public class JWTAuthEntryPoint implements AuthenticationEntryPoint {

  /**
   * Responds with a 401 Unauthorized error when an unauthenticated request is made.
   *
   * @param request       the HTTP request
   * @param response      the HTTP response
   * @param authException the exception triggered during authentication
   * @throws IOException      if an input or output error occurs
   * @throws ServletException if a servlet-specific error occurs
   */
  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    // Send a 401 Unauthorized response with the exception message
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
  }
}
