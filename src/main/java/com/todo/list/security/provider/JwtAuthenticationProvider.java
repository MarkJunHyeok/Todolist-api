package com.todo.list.security.provider;

import static com.todo.list.security.component.JwtType.ACCESS;

import com.todo.list.security.component.JwtComponent;
import com.todo.list.security.user.DefaultTodoUser;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final JwtComponent jwtComponent;

  @Override
  public Authentication authenticate(Authentication authentication) {
    String token = (String) authentication.getPrincipal();

    jwtComponent.validate(token, ACCESS);

    Long id = jwtComponent.getId(token);

    return new UsernamePasswordAuthenticationToken(
        new DefaultTodoUser(id), token, new ArrayList<>());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
