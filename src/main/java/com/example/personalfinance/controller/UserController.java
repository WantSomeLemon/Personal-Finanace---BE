package com.example.personalfinance.controller;

import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
  private final UserService userService;
  private final UserRepository userRepository;
  

}
