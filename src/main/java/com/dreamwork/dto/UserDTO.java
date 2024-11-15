package com.dreamwork.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

  private String username;
  private String password;
  private String name;
  private String lastname;
  private String role;

  public UserDTO(String username, String password, String name, String lastname, String role) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.lastname = lastname;
    this.role = role;
  }

  public UserDTO() {
  }
}
