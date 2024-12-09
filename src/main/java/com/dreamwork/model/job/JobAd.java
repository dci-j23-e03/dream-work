package com.dreamwork.model.job;

import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a job ad in the system
 *
 * This entity includes information about the job's position, company, location, and
 * required qualifications. It also establishes relationships with the recruiter who
 * posted the job ad and the candidates who applied.
 *
 * It uses lombok annotations to generate getters, setters and a no-argument constructor.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class JobAd {

  /**
   * Unique identifier for the job ad.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long jobAdId;

  /**
   * Position of the job.
   * This field is required and cannot be null.
   */
  @Column(nullable = false)
  private String position;

  /**
   * The date the job ad was posted.
   * This field is required and cannot be null.
   */
  @Column(nullable = false)
  private LocalDate date;

  /**
   * Name of the company offering the job.
   * This field is required and cannot be null.
   */
  @Column(nullable = false)
  private String company;

  /**
   * Country where the job is located.
   * This field is required and cannot be null.
   */
  @Column(nullable = false)
  private String country;

  /**
   * City where the job is located.
   * This field is required and cannot be null.
   */
  @Column(nullable = false)
  private String city;

  /**
   * The seniority level required for the job (e.g. INTERN, JUNIOR, MID, SENIOR, LEAD).
   * This field is required, cannot be null and stored as a string in the database.
   */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Seniority seniority;

  /**
   * The main technology stacks and skills required for the job.
   * This field is required and cannot be null.
   */
  @Column(nullable = false)
  private String mainTechStack;

  /**
   * A detailed description of the job, including requirements and benefits.
   * This field is required and has a maximum length of 5000 characters.
   */
  @Column(nullable = false, length = 5000)
  private String description;

  /**
   * The recruiter who posted the job ad.
   * This establishes a many-to-one relationship with the Recruiter entity.
   * Circular references are avoided with @JsonIgnoreProperties annotation.
   */
  @ManyToOne
  @JoinColumn(name = "recruiter_id", referencedColumnName = "userId")
  @JsonIgnoreProperties("jobAds")
  private Recruiter recruiter;

  /**
   * The candidates who have applied to the job.
   * This establishes a many-to-many relationship with the Candidate entity.
   * Circular references are avoided with @JsonIgnoreProperties annotation.
   */
  @ManyToMany(mappedBy = "appliedJobAds")
  @JsonIgnoreProperties("appliedJobAds") // To avoid circular references with Candidate
  private List<Candidate> candidates = new ArrayList<>();
}
