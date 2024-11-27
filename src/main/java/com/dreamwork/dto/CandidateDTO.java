package com.dreamwork.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CandidateDTO {

  private Long id;
  private String name;
  private String lastname;
  private String email;
}
