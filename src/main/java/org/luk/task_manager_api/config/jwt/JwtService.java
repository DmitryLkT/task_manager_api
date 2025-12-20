package org.luk.task_manager_api.config.jwt; 

import org.luk.task_manager_api.config.CustomUserDetails;
import org.luk.task_manager_api.dto.JwtAuthentication;
import org.luk.task_manager_api.exception.InvalidRefreshTokenException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
  @Value("${JWT_SECRET_KEY}")
  private String jwtSecretKey;

  @Value("${jwt.expiration}")
  private Long jwtExpiration;
  
  @Value("${jwt.refresh-expiration}")
  private Long refreshExpiration;

  private final UserDetailsService userDetailsService;

  //генерирует access-токен
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    if(userDetails instanceof CustomUserDetails customUserDetails) {
      claims.put("id", customUserDetails.getId());
      claims.put("email", customUserDetails.getUsername());

      String roleName = customUserDetails.getRole().getName();
      
      claims.put("role", roleName);      
    }

    return Jwts.builder()
              .setClaims(claims)
              .setSubject(userDetails.getUsername())
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
              .signWith(getSignInKey())
              .compact();
  }

  //генерирует refresh-токен
  public String generateRefreshToken(UserDetails userDetails) {
    return Jwts.builder()
              .setSubject(userDetails.getUsername())
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
              .signWith(getSignInKey())
              .compact();
  }

  //обновляет токены, если они не валидны
  public JwtAuthentication refreshTokens(String refreshToken) {
    if(!isTokenValid(refreshToken)) {
      throw new InvalidRefreshTokenException(refreshToken);
    }

    String email = extractEmail(refreshToken);
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    String newAccessToken = generateToken(userDetails);
    String newRefreshToken = generateRefreshToken(userDetails);

    return new JwtAuthentication(newAccessToken, newRefreshToken);
  }

  //Создает корректный ключ
  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  //Проверяет валидность токена
  public boolean isTokenValid(String token) {
    try {
      Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
      
      Date expiration = claims.getExpiration();
      return expiration.after(new Date());  
    } catch(JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  //Извлекаем Email из токена
  public String extractEmail(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }
}