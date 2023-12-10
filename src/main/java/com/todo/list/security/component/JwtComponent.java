package com.todo.list.security.component;

import static com.todo.list.core.util.Preconditions.notNull;
import static com.todo.list.security.component.JwtType.ACCESS;
import static com.todo.list.security.component.JwtType.REFRESH;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.todo.list.security.properties.JwtProperties;
import com.todo.list.security.user.TodoUserException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtComponent {
  private final JwtProperties jwtProperties;

  public String issue(Long id, JwtType type) {
    val now = new Date();
    val expiration = new Date(now.getTime() + jwtProperties.getTokenExpireTime(type));

    return Jwts.builder()
        .setSubject("CodeLap User " + type.name() + " Api Token")
        .setIssuer("CodeLap")
        .setIssuedAt(now)
        .setId(id.toString())
        .setExpiration(expiration)
        .signWith(HS256, jwtProperties.getEncodedSecretKey())
        .compact();
  }

  public Long getId(String token) {
    try {
      return Long.valueOf(
          Jwts.parser()
              .setSigningKey(jwtProperties.getEncodedSecretKey())
              .parseClaimsJws(token)
              .getBody()
              .getId());
    } catch (ExpiredJwtException ex) {
      return Long.valueOf(ex.getClaims().getId());
    } catch (Exception ex) {
      throw new TodoUserException("Invalid Signature", UNAUTHORIZED);
    }
  }

  public String resolveToken(HttpServletRequest req, JwtType type) {
    String header = null;
    if (type == ACCESS) {
      header = req.getHeader("Authorization");
    } else if (type == REFRESH) {
      header = req.getHeader("X-Refresh-Token");
    }

    if (header != null && header.startsWith("Bearer ")) {
      return header.substring(7);
    }
    return null;
  }

  public void isExpired(String token) {
    try {
      notNull(
          Jwts.parser()
              .setSigningKey(jwtProperties.getEncodedSecretKey())
              .parseClaimsJws(token)
              .getBody());

      throw new TodoUserException("Not expired access token", UNAUTHORIZED);
    } catch (ExpiredJwtException ignored) {
    } catch (IllegalArgumentException ex) {
      throw new TodoUserException("Invalid access token", UNAUTHORIZED);
    }
  }

  public void validate(String token, JwtType type) {
    try {
      notNull(
          Jwts.parser()
              .setSigningKey(jwtProperties.getEncodedSecretKey())
              .parseClaimsJws(token)
              .getBody());
    } catch (ExpiredJwtException ex) {
      throw new TodoUserException("Expired " + type.name() + " token", UNAUTHORIZED);
    } catch (Exception ex) {
      throw new TodoUserException("Invalid " + type.name() + " token", UNAUTHORIZED);
    }
  }
}
