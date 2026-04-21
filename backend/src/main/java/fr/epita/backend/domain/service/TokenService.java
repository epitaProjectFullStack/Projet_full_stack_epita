package fr.epita.backend.domain.service;

import fr.epita.backend.data.model.UserModel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
  private final Key signingKey;
  private final long expirationMs;

  public TokenService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.expiration-ms}")
                      long expirationMs) {
    this.signingKey =
        Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expirationMs;
  }

  public String generateToken(UserModel user) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + expirationMs);
    return Jwts.builder()
        .issuedAt(now)
        .expiration(expiration)
        .subject(user.getUuid().toString())
        .claim("login", user.getLogin())
        .claim("role", user.getRole().name())
        .signWith(this.signingKey)
        .compact();
  }
}
