package com.dreamwork.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

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