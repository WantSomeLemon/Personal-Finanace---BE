package com.example.personalfinance.service.impl;

import com.example.personalfinance.service.EmailService;
import com.example.personalfinance.util.OTPStorage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    // Constructor-injected dependencies: JavaMailSender for sending emails, OTPStorage for generating OTPs
    private final JavaMailSender mailSender;
    private final OTPStorage otpStorage;

    /**
     * Sends a verification email with a unique security code (OTP) to the provided email address.
     *
     * @param email The email address to send the verification email to.
     * @throws MailException if there is an error while sending the email.
     * @throws UnsupportedEncodingException if the email encoding is not supported.
     * @throws MessagingException if there is a problem with creating or sending the email.
     */
    @Override
    public void sendVerificationEmail(String email) throws MailException, UnsupportedEncodingException, MessagingException {

        // Set up the sender's email and name
        String fromAddress = "bach.nt.2150@aptechlearning.edu.vn"; // Sender's email address
        String senderName = "Personal Finance Team"; // Sender's name

        // Subject of the email
        String subject = "Verification Email";

        // HTML content for the email, including a placeholder for the OTP code
        String content = "<div>\n" +
                "    <span style=\"color:#808080;padding: 2px;font-family: sans-serif;\">Personal Finance Account</span><br>\n" +
                "    <span style=\"color:#5C6AC4;padding: 2px;font-size:32px;font-family: sans-serif;\"><b>Security code</b></span><br><br>\n" +
                "    <span style=\"font-family: sans-serif;\">Please use the following security code for the Personal Finance account.</span><br><br><br>\n" +
                "    <span style=\"font-family: sans-serif;\">Security code: <b>[[CODE]]</b></span><br><br><br>\n" +
                "    <span style=\"font-family: sans-serif;\">Thanks,</span><br>\n" +
                "    <span style=\"font-family: sans-serif;\">The Personal Finance Team</span>\n" +
                "</div>";

        // Create a MIME message (multi-part email)
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Set the sender, recipient, and subject of the email
        helper.setFrom(fromAddress, senderName);
        helper.setTo(email);
        helper.setSubject(subject);

        // Generate a unique OTP for the given email using OTPStorage
        String code = otpStorage.generateOTP(email);  // This generates and returns a unique OTP

        // Replace the placeholder in the email content with the generated OTP
        content = content.replace("[[CODE]]", code);

        // Set the email content as HTML and send the email
        helper.setText(content, true);  // Set to `true` for HTML content

        // Send the email
        mailSender.send(message);
    }
}
