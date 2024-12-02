package com.dreamwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

  private String username;
  private String password;
  private String name;
  private String lastname;
  private String email;
  private String role;

  public UserDTO(String username, String password, String name, String lastname, String email) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.lastname = lastname;
    this.email = email;
  }
}
