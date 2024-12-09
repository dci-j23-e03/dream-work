package com.dreamwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for the User entity
 * <p>
 * This class is used for transferring User data between service and controller. It keeps the
 * necessary fields with User's information, such as username, passwor, name, lastname, email and
 * role.
 * <p>
 * It uses lombok annotations to generate getters, setters and a no-argument constructor.
 * <p>
 * This DTO class provides a simplified representation of the User's details, typically used for
 * display purposes, or communication with external systems.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

  /**
   * The username of the user.
   */
  private String username;

  /**
   * The password of the user.
   */
  private String password;

  /**
   * The first name of the user.
   */
  private String name;

  /**
   * The last name of the user.
   */
  private String lastname;

  /**
   * The email address of the user.
   */
  private String email;

  /**
   * The role of the user (e.g., "ADMIN", "USER").
   */
  private String role;

  /**
   * Constructor for a UserDTO with the provided username, password, name, last name, and
   * email.
   * The role field is not included in this constructor and can be set later. *
   *
   * @param username the username of the user.
   * @param password the password of the user.
   * @param name     the first name of the user.
   * @param lastname the last name of the user.
   * @param email    the email address of the user.
   */
  public UserDTO(String username, String password, String name, String lastname, String email) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.lastname = lastname;
    this.email = email;
  }
}
