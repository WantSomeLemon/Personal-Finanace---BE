package com.example.personalfinance.service.impl;

import com.example.personalfinance.bean.request.*;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.UserService;
import com.example.personalfinance.util.OTPStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final JWTGenerator jwtGenerator;
  private final OTPStorage otpStorage;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
//  private final JavaMailSender mailSender;

  @Override
  public ResponseEntity<BaseResponse> register(User user) {
    return null;
  }

  @Override
  public void updateUserProfileImage(ProfileImgRequest profileImg, String userName) {

  }

  @Override
  public void updateUserProfileName(ProfileNameRequest profileName, String userName) {

  }

  @Override
  public void updateUserProfileEmail(ProfileEmailRequest profileEmail, String userName) {

  }

  @Override
  public void sendVerificationEmail(String email) throws MailException, UnsupportedEncodingException {

  }

  @Override
  public ResponseEntity<BaseResponse> updatePassord(ProfilePasswordRequest profilePassword, String userName) {
    return null;
  }

  @Override
  public ResponseEntity<BaseResponse> login(LoginRequest user) {
    return null;
  }

  @Override
  public void newPassword(String email, String password) {

  }
}
