package com.example.personalfinance.service;

import java.io.UnsupportedEncodingException;

import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

@Service
public interface EmailService {
    void sendVerificationEmail(String email) throws MailException, UnsupportedEncodingException, MessagingException;
}
