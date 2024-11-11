package com.dreamwork.model.user;

import com.dreamwork.model.job.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
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

  public User(String username, String password, String name, String lastname, Role role) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.lastname = lastname;
    this.role = role;
  }

  public User() {}
}
