package com.dreamwork.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobAdDTO {

  private String position;
  private String country;
  private String city;
  private String seniority;
  private String mainTechStack;
  private String description;

  public JobAdDTO(String position, String country, String city, String seniority,
                  String mainTechStack, String description) {
    this.position = position;
    this.country = country;
    this.city = city;
    this.seniority = seniority;
    this.mainTechStack = mainTechStack;
    this.description = description;
  }
}
