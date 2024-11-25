package com.example.personalfinance.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.personalfinance.bean.request.LoginRequest;
import com.example.personalfinance.bean.request.ProfileEmailRequest;
import com.example.personalfinance.bean.request.ProfileImgRequest;
import com.example.personalfinance.bean.request.ProfileNameRequest;
import com.example.personalfinance.bean.request.ProfilePasswordRequest;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.UserService;
import com.example.personalfinance.util.OTPStorage;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;
    private final OTPStorage otpStorage;
    private final JavaMailSender mailSender;

    /**
     * Updates the user's profile image.
     * @param profileImg The profile image to be updated.
     * @param userName The email of the user to update.
     */
    @Override
    public void updateUserProfileImage(ProfileImgRequest profileImg, String userName) {
        try {
            User user = userRepository.findByEmail(userName).orElseThrow();
            user.setProfileImage(profileImg.getImage().getBytes());
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update profile image", e);
        }
    }

    /**
     * Updates the user's first and last name.
     * @param profileName The user's updated first and last name.
     * @param userName The email of the user to update.
     */
    @Override
    public void updateUserProfileName(ProfileNameRequest profileName, String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        user.setFirstName(profileName.getFirstName());
        user.setLastName(profileName.getLastName());
        userRepository.save(user);
    }

    /**
     * Updates the user's email address.
     * @param profileEmail The user's updated email.
     * @param userName The email of the user to update.
     */
    @Override
    public void updateUserProfileEmail(ProfileEmailRequest profileEmail, String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        user.setEmail(profileEmail.getEmail());
        userRepository.save(user);
    }

    /**
     * Sends a verification email to the user with a security code.
     * @param email The email to send the verification code to.
     */
    @Override
    public void sendVerificationEmail(String email) throws MessagingException, UnsupportedEncodingException {
        String fromAddress = "PersonalFinance@outlook.com"; // Placeholder, need real email for production
        String senderName = "Personal Finance Team";
        String subject = "Personal Finance Team account security code";
        String content = "<div>\n" +
                "    <span style=\"color:#808080;padding: 2px;font-family: sans-serif;\">Paymint Account</span><br>\n" +
                "    <span style=\"color:#5C6AC4;padding: 2px;font-size:32px;font-family: sans-serif;\"><b>Security code</b></span><br><br>\n" +
                "    <span style=\"font-family: sans-serif;\">Please use the following security code for the Paymint account.</span><br><br><br>\n" +
                "    <span style=\"font-family: sans-serif;\">Security code: <b>[[CODE]]</b></span><br><br><br>\n" +
                "    <span style=\"font-family: sans-serif;\">Thanks,</span><br>\n" +
                "    <span style=\"font-family: sans-serif;\">The Paymint Team</span>\n" +
                "</div>";
        // Implement email sending using JavaMailSender with the content, subject, and sender details
    }

    /**
     * Resets the user's password.
     * @param email The email of the user requesting the password reset.
     * @param password The new password.
     */
    @Override
    public void newPassword(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    /**
     * Updates the user's password.
     * @param profilePassword The user's old and new passwords.
     * @param userName The email of the user requesting the password change.
     */
    @Override
    public ResponseEntity<BaseResponse> updatePassord(ProfilePasswordRequest profilePassword, String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();

        // Verify that the old password matches the current password
        if (new BCryptPasswordEncoder().matches(profilePassword.getOldPassword(), user.getPassword())) {
            // Check if new password is the same as the old password
            if (new BCryptPasswordEncoder().matches(profilePassword.getNewPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body(new BaseResponse("New Password can't be same as Old Password!", null));
            }
            // Update password if old password is correct and new password is different
            user.setPassword(passwordEncoder.encode(profilePassword.getNewPassword()));
            userRepository.save(user);
            return ResponseEntity.ok(new BaseResponse("Password Updated Successfully", null));
        }
        return ResponseEntity.badRequest().body(new BaseResponse("Old Password does not match", null));
    }

    /**
     * Registers a new user.
     * @param user The user information to register.
     */
    @Override
    public ResponseEntity<BaseResponse> register(User user) {
        // Check if the email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new BaseResponse("Email Already Exists", null));
        }
        // Encode the user's password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new BaseResponse("User Registered Successfully", user));
    }

    /**
     * Authenticates and logs in the user.
     * @param user The login credentials.
     */
    @Override
    public ResponseEntity<BaseResponse> login(LoginRequest user) {
        // Retrieve user by email
        User userEntity = userRepository.findByEmail(user.getEmail()).orElse(null);

        // Validate credentials
        if (userEntity == null || !new BCryptPasswordEncoder().matches(user.getPassword(), userEntity.getPassword())) {
            return ResponseEntity.badRequest().body(new BaseResponse("Incorrect Email or Password...", null));
        }

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String token = jwtGenerator.generateToken(authentication);
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token generation failed.");
        }

        // Prepare response data
        Map<Object, Object> data = new HashMap<>();
        data.put("token", token);

        // Return response with the token
        return ResponseEntity.ok(new BaseResponse("Login Success", data));
    }

    /**
     * Retrieves the user's profile using the provided JWT token.
     * @param token The JWT token to validate and extract the user details.
     */
    @Override
    public ResponseEntity<BaseResponse> getUserProfile(String token) {
        // Validate token
        if (!jwtGenerator.validateToken(jwtGenerator.getTokenFromHeader(token))) {
            return ResponseEntity.badRequest().body(new BaseResponse("Invalid Token", null));
        }

        // Get username (email) from JWT
        String email = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        User user = userRepository.findByEmail(email).orElseThrow();

        // Return user profile details
        return ResponseEntity.ok(new BaseResponse("User profile retrieved successfully", user));
    }
}
