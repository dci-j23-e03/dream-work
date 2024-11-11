package com.dreamwork.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RecruiterDTO {

  private String name;
  private String lastname;
  private String companyName;

  public RecruiterDTO(String name, String lastName, String companyName) {
    this.name = name;
    this.lastname = lastName;
    this.companyName = companyName;
  }

  public RecruiterDTO() {
  }
}
