package com.dreamwork.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JobAdDTO {

  private String position;
  private String country;
  private String city;
  private String seniority;
  private String mainTechStack;
  private String description;
  private RecruiterDTO recruiter;
  private List<CandidateDTO> candidates;

  public JobAdDTO(String position, String country, String city, String seniority,
      String mainTechStack, String description, RecruiterDTO recruiter,
      List<CandidateDTO> candidates) {
    this.position = position;
    this.country = country;
    this.city = city;
    this.seniority = seniority;
    this.mainTechStack = mainTechStack;
    this.description = description;
    this.recruiter = recruiter;
    this.candidates = candidates;
  }

  public JobAdDTO() {
    this.candidates = List.of();
  }
}
