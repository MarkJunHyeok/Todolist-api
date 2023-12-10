package com.todo.list.security.filter;

import static com.todo.list.security.component.JwtType.ACCESS;
import static com.todo.list.security.component.JwtType.REFRESH;
import static java.util.Objects.nonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.todo.list.security.component.JwtComponent;
import com.todo.list.security.user.TodoUserException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtComponent jwtComponent;
  private final AuthenticationManager authenticationManager;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var accessToken = jwtComponent.resolveToken(request, ACCESS);

    try {
      if (nonNull(accessToken)) {
        accessToken = checkRefreshFlow(request, response, accessToken);

        val authenticationToken =
            new UsernamePasswordAuthenticationToken(accessToken, "", new ArrayList<>());

        val authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (TodoUserException ex) {
      ex.printStackTrace();

      SecurityContextHolder.clearContext();

      response.setStatus(ex.getStatus().value());
      response.setContentType(APPLICATION_JSON_VALUE);
      response
          .getWriter()
          .write("{\"error\": \"UNAUTHORIZED\", \"message\": \"" + ex.getMessage() + "\"}");

      return;
    }

    filterChain.doFilter(request, response);
  }

  private String checkRefreshFlow(
      HttpServletRequest request, HttpServletResponse response, String accessToken) {
    var refreshToken = jwtComponent.resolveToken(request, REFRESH);

    if (nonNull(refreshToken)) {
      jwtComponent.isExpired(accessToken);

      jwtComponent.validate(refreshToken, REFRESH);

      val id = jwtComponent.getId(accessToken);

      accessToken = jwtComponent.issue(id, ACCESS);
      refreshToken = jwtComponent.issue(id, REFRESH);

      response.addHeader("Authorization", "Bearer " + accessToken);
      response.addHeader("X-Refresh-Token", "Bearer " + refreshToken);
    }

    return accessToken;
  }
}
