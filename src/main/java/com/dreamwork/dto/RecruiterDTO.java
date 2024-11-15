package com.dreamwork.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterDTO {

  private String name;
  private String lastname;
  private String companyName;

  public RecruiterDTO(String name, String lastname, String companyName) {
    this.name = name;
    this.lastname = lastname;
    this.companyName = companyName;
  }

  public RecruiterDTO() {
  }
}