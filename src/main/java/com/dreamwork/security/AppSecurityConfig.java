package com.dreamwork.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

  private final UserDetailsService userDetailsService;

  public AppSecurityConfig(@Autowired UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/index", "/login", "/register", "/job-ads/**").permitAll()
        .requestMatchers("/candidates/**").hasRole("CANDIDATE")
        .requestMatchers("/recruiters/**").hasRole("RECRUITER")
        .anyRequest().authenticated()
    ).formLogin(auth -> auth
            .loginPage("/login")
            .defaultSuccessUrl("/job-ads/job-ads-list", true)
            .permitAll()
    ).logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
        .logoutSuccessHandler(customLogoutSuccessHandler())
        .deleteCookies("JSESSIONID")
        .invalidateHttpSession(true)
        .permitAll()
    );

    return http.build();
  }

  @Bean
  public LogoutSuccessHandler customLogoutSuccessHandler() {
    return (request, response, authentication) -> {
      String referer = request.getHeader("Referer");
      if (referer != null) {
        response.sendRedirect(referer);
      } else {
        response.sendRedirect("/public-view/job-ad-list?logout=true");
      }
    };
  }
}
