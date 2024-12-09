package com.dreamwork.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * Data Transfer Object (DTO) for the Recruiter entity
 * <p>
 * This class is used for transferring Recruiter data between service and controller. It keeps the
 * necessary fields with Recruiter's information, such as name, lastname.
 * <p>
 * It uses lombok annotations to generate getters, setters and a constructor with all arguments.
 * <p>
 * This DTO class provides a simplified representation of the Recruiter's details, typically used
 * for display purposes, or communication with external systems.
 */
@Getter
@Setter
@AllArgsConstructor
public class RecruiterDTO {

  /**
   * The first name of the Recruiter.
   */
  private String name;

  /**
   * The last name of the Recruiter.
   */
  private String lastname;
}
