package com.dreamwork.model.user;

import com.dreamwork.model.job.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Represents a base class for all user types in the system.
 * <p>
 * This class is marked as a @MappedSuperclass to be extended by specific user roles such as
 * Candidate and Recruiter. It implements UserDetails for integration with Spring Security.
 * <p>
 * It uses lombok annotations to generate getters, setters and a no-argument constructor.
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class User implements UserDetails {

  /**
   * Unique identifier for the user.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  /**
   * The unique username of the user, used for authentication, cannot be null.
   */
  @Column(nullable = false, unique = true)
  private String username;

  /**
   * The password of the user, stored securely, cannot be null.
   */
  @Column(nullable = false)
  private String password;

  /**
   * The first name of the user, cannot be null.
   */
  @Column(nullable = false)
  private String name;

  /**
   * The last name of the user, cannot be null.
   */
  @Column(nullable = false)
  private String lastname;

  /**
   * The unique email address of the user.
   */
  @Column(nullable = false, unique = true)
  private String email;

  /**
   * The role of the user, cannot be null.
   */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  /**
   * Constructor for a user.
   *
   * @param username the username of the user
   * @param password the password of the user
   * @param name the first name of the user
   * @param lastname the last name of the user
   * @param email the email address of the user
   * @param role the role assigned to the user
   */
  protected User(String username, String password, String name, String lastname, String email,
      Role role) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.lastname = lastname;
    this.email = email;
    this.role = role;
  }

  /**
   * Returns the authorities granted to the user.
   * Currently, this method returns an empty list.
   *
   * @return a collection of granted authorities
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }
}
