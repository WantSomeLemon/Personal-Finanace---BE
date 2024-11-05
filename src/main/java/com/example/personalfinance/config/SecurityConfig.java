package com.example.personalfinance.config;

import com.example.personalfinance.config.auth.JWTAuthEntryPoint;
import com.example.personalfinance.config.auth.JWTAuthFilter;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.service.CustomUserDetailsService;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JWTAuthEntryPoint authEntryPoint;
  private final CustomUserDetailsService userDetailsService;
  private final LogoutHandler logoutHandler;
  private final JWTGenerator jwtGenerator;

  // Constructor injection for dependencies
  public SecurityConfig(CustomUserDetailsService userDetailsService, 
                        JWTAuthEntryPoint authEntryPoint, 
                        LogoutHandler logoutHandler, 
                        JWTGenerator jwtGenerator) {
    this.userDetailsService = userDetailsService;
    this.authEntryPoint = authEntryPoint;
    this.logoutHandler = logoutHandler;
    this.jwtGenerator = jwtGenerator;
  }

  // Configures security settings
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
            // Disable CSRF protection, typically needed for stateless APIs
            .csrf(csrf -> csrf.disable())
            // Configure CORS settings for cross-origin requests
            .cors(cors -> cors.configurationSource(request -> {
              var configuration = new org.springframework.web.cors.CorsConfiguration();
              configuration.setAllowedOrigins(java.util.List.of("*")); // Allow all origins
              configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE")); // Allow specific methods
              return configuration;
            }))
            // Define exception handling and set a custom authentication entry point
            .exceptionHandling(exceptionHandling ->
                    exceptionHandling.authenticationEntryPoint(authEntryPoint)
            )
            // Configure session management to be stateless, suitable for APIs
            .sessionManagement(sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Define URL authorization rules
            .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                            // Allow public access to certain endpoints
                            .requestMatchers("/api/auth/**", "/accounts", "/budget", "/goals", "/api/send-verification-email").permitAll()
                            // Require authentication for any other request
                            .anyRequest().authenticated()
            )
            // Add JWT filter before the UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            // Configure logout settings with a custom logout handler
            .logout(logout ->
                    logout
                            .logoutUrl("/api/logout") // URL for logging out
                            .addLogoutHandler(logoutHandler) // Custom handler for logout actions
                            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()) // Clear context on successful logout
            )
            // Enable HTTP Basic authentication
            .httpBasic(httpBasic -> {})
            .build();
  }

  // Expose the AuthenticationManager as a bean for authentication purposes
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  // Define a password encoder bean using BCrypt hashing
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Define the JWT authentication filter bean
  @Bean
  public JWTAuthFilter jwtAuthenticationFilter() {
    return new JWTAuthFilter(jwtGenerator, userDetailsService);
  }

  // Configures embedded Tomcat server to redirect HTTP to HTTPS
//  @Bean
//  public ServletWebServerFactory servletContainer() {
//    var tomcat = new TomcatServletWebServerFactory() {
//      @Override
//      protected void postProcessContext(Context context) {
//        var securityConstraint = new SecurityConstraint();
//        securityConstraint.setUserConstraint("CONFIDENTIAL"); // Forces HTTPS for all requests
//
//        var collection = new SecurityCollection();
//        collection.addPattern("/*"); // Apply to all URL patterns
//        securityConstraint.addCollection(collection);
//        context.addConstraint(securityConstraint);
//      }
//    };
//    // Add HTTP connector for redirecting HTTP requests to HTTPS
//    tomcat.addAdditionalTomcatConnectors(redirectConnector());
//    return tomcat;
//  }
//
//  // Configure an HTTP connector that redirects requests to HTTPS
//  private Connector redirectConnector() {
//    var connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//    connector.setScheme("http");
//    connector.setPort(8081); // HTTP port
//    connector.setSecure(false);
//    connector.setRedirectPort(8443); // Redirects to HTTPS port
//    return connector;
//  }
}
