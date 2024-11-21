package com.dreamwork.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobAdDTO {

  private String position;
  private String date;
  private String company;
  private String country;
  private String city;
  private String seniority;
  private String mainTechStack;
  private String description;
}
