package com.dreamwork.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for the Candidate entity
 * <p>
 * This class is used for transferring candidate data between service and controller. It keeps the
 * necessary fields with candidate's information, such as id, name, lastname, email.
 * <p>
 * It uses lombok annotations to generate getters, setters and a constructor with all arguments.
 * <p>
 * This DTO class provides a simplified representation of the candidate's details, typically used
 * for display purposes, or communication with external systems.
 */
@Getter
@Setter
@AllArgsConstructor
public class CandidateDTO {

  /**
   * The unique identifier of the candidate.
   */
  private Long id;

  /**
   * The first name of the candidate.
   */
  private String name;

  /**
   * The last name of the candidate.
   */
  private String lastname;

  /**
   * The email address of the candidate.
   */
  private String email;
}
