package com.dreamwork.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterDTO {

  private String name;
  private String lastname;
  private String companyName;
  private List<JobAdDTO> jobAds;

  public RecruiterDTO(String name, String lastName, String companyName) {
    this.name = name;
    this.lastname = lastName;
    this.companyName = companyName;
  }

  public RecruiterDTO() {
  }
}
