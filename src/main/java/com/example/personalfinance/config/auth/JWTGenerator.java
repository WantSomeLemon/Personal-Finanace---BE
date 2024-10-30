package com.example.personalfinance.config.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class JWTGenerator {
  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;
  public String generateToken(Authentication authentication){
    String username = authentication.getName();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + expiration);
    return Jwts.builder()
      .setSubject(username)
      .setIssuedAt(new Date())
      .setExpiration(expireDate)
      .signWith(SignatureAlgorithm.HS512, secret)
      .compact();
  }

  public String getUsernameFromJWT(String token){
    Claims claims = Jwts.parser()
      .setSigningKey(secret)
      .parseClaimsJws(token)
      .getBody();
    return claims.getSubject();
  }

  public String getTokenFromHeader(String token){
    if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
      return token.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token){
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
      return true;
    }catch (Exception e){
      throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
    }
  }
}
