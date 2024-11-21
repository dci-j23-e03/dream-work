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
}
