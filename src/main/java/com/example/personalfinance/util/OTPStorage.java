package com.example.personalfinance.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class provides functionality for storing, generating, and retrieving OTPs (One-Time Passwords)
 * associated with email addresses. It uses a thread-safe ConcurrentHashMap to store OTPs.
 */
@Component
public class OTPStorage {

  // ConcurrentHashMap is used to store OTPs, with email as the key and OTP as the value
  private final Map<String, String> otpMap = new ConcurrentHashMap<>();

  // Constant for OTP length
  private static final int OTP_Length = 6;

  /**
   * Generates a new OTP for the given email and stores it in the otpMap.
   *
   * @param email the email address for which the OTP is being generated
   * @return the generated OTP
   */
  public String generateOTP(String email){
    // Generate a random OTP
    String otp = generateRandomOTP();
    // Store the OTP in the otpMap with email as the key
    otpMap.put(email, otp);
    return otp;
  }

  /**
   * Retrieves the OTP stored for a given email.
   *
   * @param email the email address for which the OTP is being retrieved
   * @return the OTP associated with the email, or null if not found
   */
  public String getOTP(String email){
    return otpMap.get(email);
  }

  /**
   * Removes the OTP entry associated with the given email from the otpMap.
   *
   * @param email the email address for which the OTP is to be removed
   */
  public void removeOTP(String email){
    otpMap.remove(email);
  }

  /**
   * Generates a random OTP of fixed length (6 digits).
   *
   * @return a random OTP string of length 6
   */
  private String generateRandomOTP(){
    // Generate a random number and ensure it has the correct number of digits (6 in this case)
    int otpInt = new Random().nextInt((int) Math.pow(10, OTP_Length));
    return String.format("%0" + OTP_Length + "d", otpInt); // Format the number to be zero-padded to the correct length
  }
}
