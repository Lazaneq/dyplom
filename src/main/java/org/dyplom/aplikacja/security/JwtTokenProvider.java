package org.dyplom.aplikacja.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.dyplom.aplikacja.model.CustomUserDetails;
import org.dyplom.aplikacja.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

  public final String JWT_SECRET = "my_secret_key";
  private static final String AUTH_HEADER = "Authorization";
  private static final String TOKEN_PREFIX = "Bearer ";

  public String generateToken(Authentication authentication) {
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
    User user = customUserDetails.getUser();
    Date now = new Date();
    long JWT_EXPIRATION = 604800000L;
    Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

    return Jwts.builder()
        .setSubject(user.getUsername())
        .claim("role", user.getRole())
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(JWT_SECRET)
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject();
  }


  public String getTokenFromRequest(final HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTH_HEADER);

    if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
      return bearerToken.substring(TOKEN_PREFIX.length());
    }
    return null;
  }
}