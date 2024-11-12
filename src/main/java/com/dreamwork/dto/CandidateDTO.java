package com.dreamwork.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateDTO {

  private String name;
  private String lastname;
  private String country;
  private List<JobAdDTO> appliedJobAds;

  public CandidateDTO(String name, String lastName, String country) {
    this.name = name;
    this.lastname = lastName;
    this.country = country;
  }

  public CandidateDTO() {
  }
}
