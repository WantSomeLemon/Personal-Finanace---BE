package com.example.personalfinance.config;

import com.example.personalfinance.config.auth.JWTAuthEntryPoint;
import com.example.personalfinance.config.auth.JWTAuthFilter;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Injecting necessary dependencies through constructor
    private final JWTAuthEntryPoint authEntryPoint;
    private final CustomUserDetailsService userDetailsService;
    private final LogoutHandler logoutHandler;
    private final JWTGenerator jwtGenerator;

    // Constructor for initializing dependencies
    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JWTAuthEntryPoint authEntryPoint,
                          LogoutHandler logoutHandler,
                          JWTGenerator jwtGenerator) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.logoutHandler = logoutHandler;
        this.jwtGenerator = jwtGenerator;
    }

    /**
     * Configures HTTP security for the application.
     * - Stateless session management (no server-side session storage).
     * - Defines custom exception handling for unauthorized access.
     * - Configures CORS with allowed origins, methods, and headers.
     * - Sets up JWT authentication filter and CSRF protection.
     *
     * @param http the HttpSecurity object
     * @return the configured SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
                .exceptionHandling(
                        exception -> exception.authenticationEntryPoint(authEntryPoint).accessDeniedPage("/403")) // Custom entry point for unauthenticated access
                .authorizeHttpRequests(Authorize -> Authorize
                        .requestMatchers("/**", "oauth2/**", "/api/auth/**").permitAll() // Allow all requests to certain endpoints
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs").permitAll() // Allow Swagger UI and docs
                        .requestMatchers("/api/**").permitAll() // Allow all requests to /api endpoints
                        .anyRequest().permitAll()) // Permit other requests
                .csrf(c -> c.disable()) // Disable CSRF for stateless authentication
                .cors(cors -> cors.configurationSource(request -> { // Configure CORS settings
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowCredentials(true); // Allow credentials
                    corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*")); // Allow all origins
                    corsConfiguration.setAllowedMethods(Collections.singletonList("*")); // Allow all HTTP methods
                    corsConfiguration.setAllowedHeaders(
                            Arrays.asList("Origin", "Content-Type", "Accept", "responseType", "Authorization")); // Allow specific headers

                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.setMaxAge(3600L); // Max age for pre-flight requests
                    return corsConfiguration;
                }))
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authEntryPoint)) // Custom authentication entry point

                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // Add JWT filter before UsernamePasswordAuthenticationFilter

                .httpBasic(Customizer.withDefaults()) // Enable basic HTTP authentication (optional)
                .formLogin(Customizer.withDefaults()) // Enable form-based login (optional)

                .build(); // Build the security configuration
    }

    /**
     * Exposes the AuthenticationManager bean for authentication purposes.
     *
     * @param authenticationConfiguration configuration object for authentication
     * @return an instance of AuthenticationManager
     * @throws Exception if an error occurs during authentication manager initialization
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Retrieve the AuthenticationManager from the configuration
    }

    /**
     * Defines the PasswordEncoder bean using BCrypt hashing for secure password storage.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for encoding passwords
    }

    /**
     * Defines the JWTAuthFilter bean, responsible for processing JWT tokens.
     *
     * @return an instance of JWTAuthFilter
     */
    @Bean
    public JWTAuthFilter jwtAuthenticationFilter() {
        return new JWTAuthFilter(jwtGenerator, userDetailsService); // Create a new JWTAuthFilter with necessary dependencies
    }
}