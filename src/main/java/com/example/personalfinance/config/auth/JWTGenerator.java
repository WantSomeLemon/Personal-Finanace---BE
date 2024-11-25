package com.example.personalfinance.config.auth;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTGenerator {

	// Secret key used to sign and verify JWT tokens
	private SecretKey key = Keys.hmacShaKeyFor("personalfinance666789789789756498789352123164".getBytes());

	/**
	 * Generates a new JWT token with the user's email as a claim and a 24-hour expiration.
	 * @param authentication the current user's authentication object
	 * @return the generated JWT token as a String
	 */
	public String generateToken(Authentication authentication) {
		String token = Jwts.builder()
				.issuedAt(new Date()) // Set the issue time to the current time
				.expiration(new Date(new Date().getTime() + 86400000)) // Token valid for 24 hours
				.signWith(key) // Sign the token with the secret key
				.claim("email", authentication.getName()) // Add the user's email as a claim
				.compact(); // Build the token as a compact string
		System.out.println("New token :");
		System.out.println(token);
		return token;
	}

	/**
	 * Validates a JWT token by parsing its claims and verifying its signature.
	 * @param token the JWT token to validate
	 * @return true if the token is valid, otherwise an exception is thrown
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
					.verifyWith(key) // Use the secret key to verify the token
					.build()
					.parseSignedClaims(token)
					.getPayload(); // Parse the token and ensure it has valid claims
			return true;
		} catch (Exception ex) {
			// Throw an exception if the token is invalid or expired
			throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect",
					ex.fillInStackTrace());
		}
	}

	/**
	 * Extracts the username (email) from the JWT token.
	 * @param token the JWT token
	 * @return the email of the user
	 * @throws JwtException if the token is invalid
	 */
	public String getUsernameFromJWT(String token) {
		try {
			Claims claims = Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token)
					.getPayload(); // Parse the token and retrieve its payload
			String email = String.valueOf(claims.get("email")); // Extract the "email" claim

			return email;
		} catch (JwtException e) {
			// Log any JWT exceptions
			System.out.println("JWT parsing error: " + e.getMessage());
			throw e;  // Rethrow to handle higher up
		}
	}

	/**
	 * Extracts the raw token from an Authorization header.
	 * @param token the Authorization header value (e.g., "Bearer <token>")
	 * @return the raw token if it exists, otherwise null
	 */
	public String getTokenFromHeader(String token) {
		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			return token.substring(7); // Remove the "Bearer " prefix
		}
		return null;
	}

	/**
	 * Checks if the token is expired.
	 * @param token the JWT token
	 * @return true if the token is expired, otherwise false
	 */
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date()); // Compare expiration time with current time
	}

	/**
	 * Extracts the expiration date of the token.
	 * @param token the JWT token
	 * @return the expiration date
	 */
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration); // Retrieve the "expiration" claim
	}

	/**
	 * Extracts the email from the token's claims.
	 * @param token the JWT token
	 * @return the email
	 */
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject); // Retrieve the "subject" claim
	}

	/**
	 * Extracts a specific claim from the token.
	 * @param token the JWT token
	 * @param claimsResolver a function to resolve the desired claim
	 * @param <T> the type of the claim
	 * @return the extracted claim
	 */
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Parses all claims from the token.
	 * @param token the JWT token
	 * @return the claims
	 */
	private Claims extractAllClaims(String token) {
		return Jwts
				.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload(); // Parse the token and retrieve all its claims
	}
}