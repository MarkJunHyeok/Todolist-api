package com.todo.list.security.config;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.todo.list.security.oauth2.CustomOAuth2UserService;
import com.todo.list.security.oauth2.OAuth2FailureHandler;
import com.todo.list.security.oauth2.OAuth2SuccessHandler;
import com.todo.list.security.provider.JwtAuthenticationProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final CustomOAuth2UserService oAuth2UserService;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;
  private final OAuth2FailureHandler oAuth2FailureHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      JwtAuthenticationProvider jwtAuthenticationProvider,
      SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> jwtFilterConfigurer)
      throws Exception {
    http.formLogin().disable();
    http.httpBasic().disable();
    http.cors().disable();
    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(STATELESS);

    http.oauth2Login()
        .authorizationEndpoint()
        .baseUri("/login/oauth2/authorization")
        .and()
        .successHandler(oAuth2SuccessHandler)
        .failureHandler(oAuth2FailureHandler)
        .userInfoEndpoint()
        .userService(oAuth2UserService);

    http.authorizeRequests().anyRequest().authenticated();

    http.authenticationProvider(jwtAuthenticationProvider);
    http.apply(jwtFilterConfigurer);

    http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(FORBIDDEN));

    http.cors().configurationSource(corsConfigurationSource());

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowCredentials(false);
    config.setAllowedOrigins(List.of("http://localhost:3000", "*", "https://vuetodo.site"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setExposedHeaders(List.of("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
