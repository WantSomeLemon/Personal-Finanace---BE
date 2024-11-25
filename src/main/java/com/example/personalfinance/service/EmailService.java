package com.example.personalfinance.service;

import jakarta.mail.MessagingException;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface EmailService {

    /**
     * Sends a verification email to the provided email address.
     * The email contains a security code for user verification.
     *
     * @param email the recipient's email address to send the verification email to
     * @throws MailException if an error occurs during email sending
     * @throws UnsupportedEncodingException if the encoding of the email content is not supported
     * @throws MessagingException if a messaging-related error occurs
     */
    void sendVerificationEmail(String email) throws MailException, UnsupportedEncodingException, MessagingException;
}
