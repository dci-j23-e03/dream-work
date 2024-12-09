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

/**
 * Security configuration class to define the security settings of the application. This includes
 * password encoding, authentication provider setup, and URL access control.
 */
@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

  private final UserDetailsService userDetailsService;

  /**
   * Constructor to inject the UserDetailsService to be used for authentication.
   *
   * @param userDetailsService the UserDetailsService for custom user authentication.
   */
  public AppSecurityConfig(@Autowired UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  /**
   * Configures a PasswordEncoder bean to encode passwords using BCrypt. This encoder is necessary
   * for handling passwords securely in the application.
   *
   * @return a PasswordEncoder instance configured with BCrypt.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configures the AuthenticationProvider bean that integrates the UserDetailsService and
   * PasswordEncoder for user authentication. The DaoAuthenticationProvider is used for
   * authenticating users from a data source (in this case, the UserDetailsService).
   *
   * @return an AuthenticationProvider bean configured with the UserDetailsService and
   * PasswordEncoder.
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  /**
   * Configures HTTP security settings for the application, including URL access control and form
   * login settings. This method specifies which URL paths are accessible by which roles, the login
   * page configuration, and logout settings.
   *
   * @param http the HttpSecurity object used to configure web-based security.
   * @return the configured SecurityFilterChain object.
   * @throws Exception if an error occurs while configuring HTTP security.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth
        // Define publicly accessible URLs
        .requestMatchers("/", "/login", "/register", "/job-ads/**").permitAll()
        // Define access rules for candidates
        .requestMatchers("/candidates/**").hasRole("CANDIDATE")
        // Define access rules for recruiters
        .requestMatchers("/recruiters/**").hasRole("RECRUITER")
        // Require authentication for any other requests
        .anyRequest().authenticated()
    ).formLogin(auth -> auth
        // Set custom login page
        .loginPage("/login")
        // Redirect to the job ads page after successful login
        .defaultSuccessUrl("/job-ads", true)
        // Allow all users to access the login page
        .permitAll()
    ).logout(logout -> logout
        // Set custom logout URL
        .logoutUrl("/logout")
        // Redirect to job ads page after successful logout
        .logoutSuccessUrl("/job-ads?logout")
        // Delete the session cookie after logout
        .deleteCookies("JSESSIONID")
        // Invalidate the session after logout
        .invalidateHttpSession(true)
        // Allow all users to access the logout functionality
        .permitAll()
    );

    return http.build();
  }
}
