package com.example.personalfinance.controller;

import com.example.personalfinance.bean.request.LoginRequest;
import com.example.personalfinance.bean.request.ProfileEmailRequest;
import com.example.personalfinance.bean.request.ProfileImgRequest;
import com.example.personalfinance.bean.request.ProfileNameRequest;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.UserService;
import com.example.personalfinance.util.OTPStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
  private UserService userService;
  private UserRepository userRepository;
  private JWTGenerator jwtGenerator;
  private OTPStorage otpStorage;

  @Autowired
  public UserController(UserRepository userRepository, JWTGenerator jwtGenerator, UserService userService, OTPStorage otpStorage) {
    this.userRepository = userRepository;
    this.jwtGenerator = jwtGenerator;
    this.userService = userService;
    this.otpStorage = otpStorage;
  }
  
  @PostMapping("/auth/register")
  public ResponseEntity<BaseResponse> register(@RequestBody User user) {
    return userService.register(user);
  }
  
  @PostMapping("/auth/login")
  public ResponseEntity<BaseResponse> login(@RequestBody LoginRequest user) {
    return userService.login(user);
  }
  
  @GetMapping("/auth/validateToken")
  public ResponseEntity<BaseResponse> home(@RequestHeader(value ="Authorization") String token) {
    Map<Object,Object> data = new HashMap<>();
    if(jwtGenerator.validateToken(jwtGenerator.getTokenFromHeader(token))) {
      Optional<User> user = userRepository.findByEmail(jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token)));
      data.put("user", user);
      return ResponseEntity.ok(new BaseResponse("success", data));
    }
    return new ResponseEntity<>(new BaseResponse("Session Expired", data), HttpStatus.UNAUTHORIZED);
  }
  
  @PostMapping("/profile/image")
  public ResponseEntity<BaseResponse> updatedProfilePicture(@RequestHeader(value = "Authorization") String token,
                                                            @ModelAttribute ProfileImgRequest profileImgRequest) {
    try{
      String username = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
      userService.updateUserProfileImage(profileImgRequest, username);
      return ResponseEntity.ok(new BaseResponse("success", profileImgRequest));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new BaseResponse("Fail to update user profile image.", e));
    }
  }
  
  @PostMapping("/profile/name")
  public ResponseEntity<BaseResponse> updateProfileName(@RequestHeader(value = "Authorization") String token, 
                                                        @RequestBody ProfileNameRequest profileNameRequest) {
    try{
      String username = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
      userService.updateUserProfileName(profileNameRequest, username);
      return ResponseEntity.ok(new BaseResponse("success", profileNameRequest));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new BaseResponse("Fail to update user profile name.", e));
    }
  }
  
  @PostMapping("/profile/email")
  public ResponseEntity<BaseResponse> updateProfileEmail(@RequestHeader(value = "Authorization") String token, 
                                                         @RequestBody ProfileEmailRequest profileEmailRequest) {
    try{
      String username = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
      userService.updateUserProfileEmail(profileEmailRequest, username);
      return ResponseEntity.ok(new BaseResponse("success", profileEmailRequest));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new BaseResponse("Fail to update user email.", e));
    }
  }
  
  @PostMapping("/auth/send-verification-email")
  public ResponseEntity<BaseResponse> sendVerificationEmail(@RequestParam(value = "email") String email, 
                                                            @RequestParam(value = "otp") String otp) {
    try{
      if(userRepository.existsByEmail(email)) {
        return ResponseEntity.badRequest().body(new BaseResponse("Email already in use", null));
      }
      
    } catch ()
  }
}
