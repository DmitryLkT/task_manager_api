import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  @Value("${JWT_SECRET_KEY}")
  private String jwtSecretKey;

  @Value("${jwt.expiration}")
  private Long jwtExpiration;
  
  @Value("${jwt.refresh-expiration}")
  private Long refreshExpiration;

  //генерирует access-токен
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    if(userDetails instanceof User customUserDetails) {
      claims.put("id", customUserDetails.getId());
      claims.put("email", customUserDetails.getEmail());

      List<String> roleName = customUserDetails.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toList());
      
      claims.put("roles", roleName);      
    }

    return Jwts.builder()
              .setClaims(claims)
              .setSubject(userDetails.getUsername())
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
              .signWith(getSingInKey())
              .compact();
  }

  //генерирует refresh-токен
  public String generateRefreshToken(UserDetails userDetails) {
    return Jwts.builder()
              .setSubject(userDetails.getUsername())
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
              .signWith(getSingInKey())
              .compact();
  }

  //обновляет токены, если они не валидны
  public JwtAuthentication refreshTokens(String refreshToken) {
    if(!isTokenValid(refreshToken)) {
      throw new RuntimeException("Invalid or expired refresh token");
    }

    String email = extractEmail(refreshToken);
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    String newAccessToken = generateToken(userDetails);
    String newRefreshToken = generateRefreshToken(userDetails);

    return new JwtAuthentication(newAccessToken, newRefreshToken);
  }

  //Создает корректный ключ
  private SecretKey getSingInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  //Проверяет валидность токена
  public boolean isTokenValid(String token) {
    try {
      Jwts.parserBuilder()
        .setSigningKey(getSingInKey())
        .build()
        .parseClaimsJws(token);
      
      Date expiration = extractExpiration(token); //
      return expiration.after(new Date());  
    } catch(JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  //Извлекаем Email из токена
  public String extractEmail(String token) {
    return Jwts.parserBuilder()
            .getSinginKey(getSingInKey())
            .build()
            .parseSignedClaims(token)
            .getBody()
            .getSubject();
  }
}
