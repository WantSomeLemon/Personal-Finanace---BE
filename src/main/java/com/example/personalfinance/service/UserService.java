package com.example.personalfinance.service;

import java.io.UnsupportedEncodingException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.personalfinance.bean.request.LoginRequest;
import com.example.personalfinance.bean.request.ProfileEmailRequest;
import com.example.personalfinance.bean.request.ProfileImgRequest;
import com.example.personalfinance.bean.request.ProfileNameRequest;
import com.example.personalfinance.bean.request.ProfilePasswordRequest;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.entity.User;

import jakarta.mail.MessagingException;

/**
 * Service interface for handling user-related operations.
 * Provides methods to update user profile, manage authentication, and send emails.
 */
@Service
public interface UserService {

    /**
     * Updates the user's profile image.
     *
     * @param profileImg the new profile image request
     * @param userName the username of the user whose profile image is to be updated
     */
    void updateUserProfileImage(ProfileImgRequest profileImg, String userName);

    /**
     * Updates the user's profile name.
     *
     * @param profileName the new profile name request
     * @param userName the username of the user whose profile name is to be updated
     */
    void updateUserProfileName(ProfileNameRequest profileName, String userName);

    /**
     * Updates the user's profile email.
     *
     * @param profileEmail the new email address
     * @param userName the username of the user whose profile email is to be updated
     */
    void updateUserProfileEmail(ProfileEmailRequest profileEmail, String userName);

    /**
     * Sends a verification email to the user.
     *
     * @param email the email address to send the verification to
     * @throws MessagingException if an error occurs while sending the email
     * @throws UnsupportedEncodingException if the email encoding is not supported
     */
    void sendVerificationEmail(String email) throws MessagingException, UnsupportedEncodingException;

    /**
     * Resets the user's password by sending a new password to their email.
     *
     * @param email the email address of the user
     * @param password the new password
     */
    void newPassword(String email, String password);

    /**
     * Updates the user's password.
     *
     * @param profilePassword the new password request
     * @param userName the username of the user whose password is to be updated
     * @return a ResponseEntity with a BaseResponse containing the result
     */
    ResponseEntity<BaseResponse> updatePassord(ProfilePasswordRequest profilePassword, String userName);

    /**
     * Registers a new user in the system.
     *
     * @param user the user to be registered
     * @return a ResponseEntity with a BaseResponse containing the result
     */
    ResponseEntity<BaseResponse> register(User user);

    /**
     * Logs in a user and generates an authentication token.
     *
     * @param user the login request containing user credentials
     * @return a ResponseEntity with a BaseResponse containing the result
     */
    ResponseEntity<BaseResponse> login(LoginRequest user);

    /**
     * Retrieves the profile information of the authenticated user.
     *
     * @param token the authentication token for the user
     * @return a ResponseEntity with the user's profile data
     */
    ResponseEntity<BaseResponse> getUserProfile(String token);
}
