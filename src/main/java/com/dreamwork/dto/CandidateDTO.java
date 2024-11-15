package com.dreamwork.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateDTO {

  private String name;
  private String lastname;
  private String country;

  public CandidateDTO(String name, String lastname, String country) {
    this.name = name;
    this.lastname = lastname;
    this.country = country;
  }

  public CandidateDTO() {
  }
}