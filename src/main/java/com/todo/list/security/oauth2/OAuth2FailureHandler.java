package com.todo.list.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

  @Value("${oauth2.frontUri}")
  private String redirectUri;

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    String targetUrl = getTargetUrl();

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  protected String getTargetUrl() {

    return UriComponentsBuilder.fromUriString(redirectUri)
        .queryParam("error", "")
        .build()
        .toUriString();
  }
}
