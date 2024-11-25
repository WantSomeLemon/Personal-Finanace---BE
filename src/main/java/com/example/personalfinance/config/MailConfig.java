package com.example.personalfinance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuration class for setting up the JavaMailSender bean.
 * This bean is used for sending emails in the application.
 */
@Configuration
public class MailConfig {

    // Inject email server configurations from application properties
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    /**
     * Configures and returns a JavaMailSender bean.
     *
     * @return an instance of JavaMailSender configured with the specified SMTP server properties.
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Set SMTP server properties
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        // Configure additional email properties
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true"); // Enable authentication
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS for secure communication
        props.put("mail.debug", "true"); // Enable debug logging for email sending

        return mailSender; // Return the configured mail sender
    }
}