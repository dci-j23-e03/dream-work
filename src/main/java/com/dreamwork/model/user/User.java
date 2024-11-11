package com.dreamwork.model.user;

import com.dreamwork.model.job.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public abstract class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  private String username;
  private String password;
  private String name;
  private String lastname;

  @Enumerated(EnumType.STRING)
  private Role role;

  protected User(String username, String password, String name, String lastname, Role role) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.lastname = lastname;
    this.role = role;
  }

  protected User() {
  }
}
