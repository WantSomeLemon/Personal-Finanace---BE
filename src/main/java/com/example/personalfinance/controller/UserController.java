package com.example.personalfinance.controller;

import com.example.personalfinance.bean.request.*;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.EmailService;
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
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private OTPStorage otpStorage;
    @Autowired
    private EmailService emailService;

    public UserController(UserRepository userRepository, JWTGenerator jwtGenerator, UserService userService, OTPStorage otpStorage, EmailService emailService) {
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
        this.otpStorage = otpStorage;
        this.emailService = emailService;
    }

    /**
     * API endpoint for user registration.
     * @param user The user information to be registered
     * @return Response with registration status
     */
    @PostMapping("/auth/register")
    public ResponseEntity<BaseResponse> register(@RequestBody User user) {
        return userService.register(user);
    }

    /**
     * API endpoint for user login.
     * @param user Login credentials (username and password)
     * @return Response with login status and JWT token
     */
    @PostMapping("/auth/login")
    public ResponseEntity<BaseResponse> login(@RequestBody LoginRequest user) {
        return userService.login(user);
    }

    /**
     * API endpoint to validate the user's JWT token.
     * @param token The JWT token to validate
     * @return Response indicating whether the token is valid
     */
    @GetMapping("/auth/validateToken")
    public ResponseEntity<BaseResponse> home(@RequestHeader(value = "Authorization") String token) {
        Map<Object, Object> data = new HashMap<>();
        if (jwtGenerator.validateToken(jwtGenerator.getTokenFromHeader(token))) {
            Optional<User> user = userRepository.findByEmail(jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token)));
            data.put("user", user);
            return ResponseEntity.ok(new BaseResponse("validated", data));
        }
        return new ResponseEntity<>(new BaseResponse("Session Expired", data), HttpStatus.UNAUTHORIZED);
    }

    /**
     * API endpoint to update the user's profile image.
     * @param token Authorization token (JWT) to identify the user
     * @param profileImgRequest Contains the new image data
     * @return Response with success or failure message
     */
    @PostMapping("/profile/image")
    public ResponseEntity<BaseResponse> updatedProfilePicture(@RequestHeader(value = "Authorization") String token,
                                                              @ModelAttribute ProfileImgRequest profileImgRequest) {
        try {
            String username = jwtGenerator.getUsernameFromJWT(token);
            userService.updateUserProfileImage(profileImgRequest, username);
            return ResponseEntity.ok(new BaseResponse("success", profileImgRequest));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse("Fail to update user profile image.", e));
        }
    }

    /**
     * API endpoint to update the user's profile name.
     * @param token Authorization token (JWT) to identify the user
     * @param profileNameRequest Contains the new name data
     * @return Response with success or failure message
     */
    @PostMapping("/profile/name")
    public ResponseEntity<BaseResponse> updateProfileName(@RequestHeader(value = "Authorization") String token,
                                                          @RequestBody ProfileNameRequest profileNameRequest) {
        try {
            String username = jwtGenerator.getUsernameFromJWT(token);
            userService.updateUserProfileName(profileNameRequest, username);
            return ResponseEntity.ok(new BaseResponse("success", profileNameRequest));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse("Fail to update user profile name.", e));
        }
    }

    /**
     * API endpoint to update the user's profile email.
     * @param token Authorization token (JWT) to identify the user
     * @param profileEmailRequest Contains the new email data
     * @return Response with success or failure message
     */
    @PostMapping("/profile/email")
    public ResponseEntity<BaseResponse> updateProfileEmail(@RequestHeader(value = "Authorization") String token,
                                                           @RequestBody ProfileEmailRequest profileEmailRequest) {
        try {
            String username = jwtGenerator.getUsernameFromJWT(token);
            userService.updateUserProfileEmail(profileEmailRequest, username);
            return ResponseEntity.ok(new BaseResponse("success", profileEmailRequest));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse("Fail to update user email.", e));
        }
    }

    /**
     * API endpoint to send a verification email for the user's email address.
     * @param email The email address to send the verification code to
     * @return Response with success or failure message
     */
    @PostMapping("/auth/send-verification-email")
    public ResponseEntity<BaseResponse> sendVerificationEmail(@RequestParam(value = "email") String email) {
        try {
            if (userRepository.existsByEmail(email)) {
                return ResponseEntity.badRequest().body(new BaseResponse("User already in use"));
            }
            emailService.sendVerificationEmail(email);
            return ResponseEntity.ok(new BaseResponse("success"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse("Fail, try again.", e));
        }
    }

    /**
     * API endpoint to verify the OTP sent to the user's email for verification.
     * @param email The email address of the user
     * @param otp The OTP to verify
     * @return Response with OTP verification status
     */
    @PostMapping("/auth/verify-security-code")
    public ResponseEntity<BaseResponse> verifyOTP(@RequestParam(value = "email") String email,
                                                  @RequestParam(value = "otp") String otp){
        String storeOTP = otpStorage.getOTP(email);
        if(storeOTP == null || !storeOTP.equals(otp)){
            return ResponseEntity.badRequest().body(new BaseResponse("Invalid OTP."));
        }
        otpStorage.removeOTP(email);
        return ResponseEntity.ok(new BaseResponse("OTP verified successfully"));
    }

    /**
     * API endpoint to initiate the password reset process by sending a verification email.
     * @param email The email address to send the password reset email to
     * @return Response with the result of the email send operation
     */
    @PostMapping("/auth/forgot-password/send-verification-email")
    public ResponseEntity<BaseResponse> forgetPasswordSendVerificationEmail(@RequestParam(value = "email") String email){
        try{
            emailService.sendVerificationEmail(email);
            return ResponseEntity.ok(new BaseResponse("success"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new BaseResponse("Fail, try again.", e));
        }
    }

    /**
     * API endpoint to set a new password for the user after they have verified their email.
     * @param email The email address of the user
     * @param password The new password
     * @return Response with success or failure message
     */
    @PutMapping("/auth/new-password")
    public ResponseEntity<BaseResponse> newPassword(@RequestParam(value = "email") String email,
                                                    @RequestParam(value = "password") String password){
        try{
            userService.newPassword(email, password);
            return ResponseEntity.ok(new BaseResponse("success"));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(new BaseResponse("Failed to update user profile password!", e));
        }
    }

    /**
     * API endpoint to update the user's password.
     * @param token Authorization token (JWT) to identify the user
     * @param profilePasswordRequest Contains the new password
     * @return Response with success or failure message
     */
    @PutMapping("/profile/password")
    public ResponseEntity<BaseResponse> updatePassword(@RequestParam(value = "Authorization") String token,
                                                       @RequestBody ProfilePasswordRequest profilePasswordRequest){
        try{
            String username = jwtGenerator.getUsernameFromJWT(token);
            return userService.updatePassord(profilePasswordRequest, username);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(new BaseResponse("Failed to update user profile password!", e));
        }
    }

    /**
     * API endpoint to retrieve the user's profile information.
     * @param jwt The user's JWT token for authorization
     * @return Response with the user's profile data
     */
    @GetMapping("/profile")
    public ResponseEntity<BaseResponse> getUserProfileHandler(@RequestHeader("Authorization") String jwt) {
        try {
            return userService.getUserProfile(jwt);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse("Failed to retrieve user profile.", e));
        }
    }
}
