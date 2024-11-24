package org.dyplom.aplikacja.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

  private final String JWT_SECRET = "your_secret_key"; // Użyj mocnego klucza
  private final long JWT_EXPIRATION = 604800000L; // 7 dni

  public String generateToken(Authentication authentication) {
    String username = authentication.getName();
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
        .compact();
  }

  // Metoda do walidacji tokenu
  public boolean validateToken(String token) {
    // Implementacja walidacji tokenu
    return true;
  }

  // Metoda do pobierania username z tokenu
  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(JWT_SECRET)
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject();
  }
}
