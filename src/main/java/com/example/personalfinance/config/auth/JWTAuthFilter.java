package com.example.personalfinance.config.auth;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.personalfinance.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * A custom filter that intercepts requests to validate and parse JWTs.
 * Ensures that authenticated users are set in the Spring Security context
 * if a valid JWT is present in the request header.
 */

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
    final private JWTGenerator tokenGenerator;
    final private CustomUserDetailsService customUserDetailsService;

    /**
     * Intercepts each request to check for a valid JWT token in the Authorization header.
     * If the token is valid, it sets the authentication in the SecurityContext.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an input or output error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // Extract the JWT from the request
        String token = getJWTFromRequest(request);
        // Validate the token and set authentication in the SecurityContext if valid
        if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
            String username = tokenGenerator.getUsernameFromJWT(token);

            // Load the user details from the database or another source
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            // Create an authentication token with user details and authorities
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities());

            // Set additional details for the authentication
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set the authentication in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }


    /**
     * Extracts the JWT token from the "Authorization" header of the HTTP request.
     *
     * @param request the HTTP request
     * @return the JWT token if present and valid, or null otherwise
     */
    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Check if the header contains a valid Bearer token
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Extract and return the token (remove "Bearer " prefix)
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
