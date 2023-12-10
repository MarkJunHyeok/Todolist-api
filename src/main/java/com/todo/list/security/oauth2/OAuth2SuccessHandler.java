package com.todo.list.security.oauth2;

import static com.todo.list.security.component.JwtType.ACCESS;
import static com.todo.list.security.component.JwtType.REFRESH;

import com.todo.list.common.user.domain.User;
import com.todo.list.common.user.service.UserService;
import com.todo.list.security.component.JwtComponent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final UserService userService;
  private final JwtComponent jwtComponent;

  @Value("${oauth2.frontUri}")
  private String redirectUri;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

    Object attribute = oAuth2User.getAttribute("socialId");
    Long socialId = Long.parseLong(Objects.requireNonNull(attribute).toString());

    User user = userService.loadUser(socialId);

    String accessToken = jwtComponent.issue(user.getId(), ACCESS);
    String refreshToken = jwtComponent.issue(user.getId(), REFRESH);

    String targetUrl = getTargetUrl(accessToken, refreshToken);

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  protected String getTargetUrl(String accessToken, String refreshToken) {

    return UriComponentsBuilder.fromUriString(redirectUri)
        .queryParam("accessToken", accessToken)
        .queryParam("refreshToken", refreshToken)
        .build()
        .toUriString();
  }
}
