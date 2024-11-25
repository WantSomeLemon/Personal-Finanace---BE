package com.example.personalfinance.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * Loads a user by their email for authentication purposes.
   *
   * This method is called by Spring Security during the authentication process.
   * It retrieves the user's details from the database based on their email.
   * If no user is found, a UsernameNotFoundException is thrown.
   *
   * @param email the email of the user to be loaded
   * @return a UserDetails object representing the user
   * @throws UsernameNotFoundException if the user is not found in the database
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    // Retrieve the user from the database using the email
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

    // Return a UserDetails object for Spring Security with the user's email, password, and an empty list of authorities
    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
            Collections.emptyList());
  }
}
