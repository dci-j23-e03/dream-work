package com.dreamwork.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobAdDTO {

  private Long id;
  private String position;
  private LocalDate date;
  private String company;
  private String country;
  private String city;
  private String seniority;
  private String mainTechStack;
  private String description;
}
