package com.example.personalfinance.util;

import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.AccountRepository;
import com.example.personalfinance.repository.CategoryRepository;
import com.example.personalfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final CategoryRepository categoryRepository;
  private final AccountRepository accountRepository;
  
  static User savedUser;


  private User createUser(String email, String rawPassword, String firstName, String lastName) {
    User user = new User();
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(rawPassword));
    user.setFirstName(firstName);
    user.setLastName(lastName);
    return user;
  }

  public void saveUser() {
    List<User> users = List.of(
            createUser("user1@gmail.com", "password1", "User", "One"),
            createUser("user2@gmail.com", "password2", "User", "Two"),
            createUser("user3@gmail.com", "password3", "User", "Three"),
            createUser("user4@gmail.com", "password4", "User", "Four"),
            createUser("user5@gmail.com", "password5", "User", "Five")
    );

    userRepository.saveAll(users);
  }
  

  @Override
  public void run(String... args) throws Exception {
    if(!userRepository.existsByEmail("user1@gmail.com")
            && !userRepository.existsByEmail("user2@gmail.com")
            && !userRepository.existsByEmail("user3@gmail.com")
            && !userRepository.existsByEmail("user4@gmail.com")
            && !userRepository.existsByEmail("user5@gmail.com")
    ){
      saveUser();
    }
  }
}
