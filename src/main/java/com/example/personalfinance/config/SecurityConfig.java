package com.example.personalfinance.config;

import com.example.personalfinance.config.auth.JWTAuthEntryPoint;
import com.example.personalfinance.service.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private JWTAuthEntryPoint authEntryPoint;
  private CustomUserDetailsService userDetailsService;
  private final LogoutHandler logoutHandler;
  public final SecurityConfig(CustomUserDetailsService userDetailsService, JWTAuthEntryPoint authEntryPoint){
    this.userDetailsService = userDetailsService;
    this.authEntryPoint = authEntryPoint;
  }
}
