package com.dreamwork.model.user;

import com.dreamwork.model.job.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String lastname;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
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
