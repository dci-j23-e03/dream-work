package com.dreamwork.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * Data Transfer Object (DTO) for the JobAd entity
 * <p>
 * This class is used for transferring JobAd data between service and controller. It keeps the
 * necessary fields with JobAd's information, such as id, position, date, company, country, city,
 * seniority, main tech stack and description.
 * <p>
 * It uses lombok annotations to generate getters, setters and a constructor with all arguments.
 * <p>
 * This DTO class provides a simplified representation of the JabAd's details, typically used for
 * display purposes, or communication with external systems.
 */
@Getter
@Setter
@AllArgsConstructor
public class JobAdDTO {

  /**
   * The unique identifier of the job ad.
   */
  private Long id;

  /**
   * The job position or title of the ad (e.g., "Software Developer").
   */
  private String position;

  /**
   * The date when the job ad was posted.
   */
  private LocalDate date;

  /**
   * The name of the company posting the job ad.
   */
  private String company;

  /**
   * The country where the job is located.
   */
  private String country;

  /**
   * The city where the job is located.
   */
  private String city;

  /**
   * The seniority level required for the job (e.g., "JUNIOR", "MID", "SENIOR").
   */
  private String seniority;

  /**
   * The main technology stack used in the job (e.g., "Java, Spring, PostgreSQL").
   */
  private String mainTechStack;

  /**
   * A description of the job role and responsibilities.
   */
  private String description;
}
