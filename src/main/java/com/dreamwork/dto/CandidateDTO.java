package com.dreamwork.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CandidateDTO {

  private String name;
  private String lastname;
  private String country;

  public CandidateDTO(String name, String lastName, String country) {
    this.name = name;
    this.lastname = lastName;
    this.country = country;
  }

  public CandidateDTO() {
  }
}
