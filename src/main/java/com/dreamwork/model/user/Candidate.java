package com.dreamwork.model.user;

import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.job.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Represents a candidate user in the system.
 * <p>
 * A candidate can apply for multiple job ads and upload a CV as part of their profile. This entity
 * extends the User class to inherit common user attributes.
 * <p>
 * It uses lombok annotations to generate getters, setters and a no-argument constructor.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Candidate extends User {

  /**
   * A list of job ads the candidate has applied for
   * <p>
   * This is a many-to-many relationship with JobAd It uses a join table named "applied_jobs" to
   * manage the association.
   * <p>
   * Circular references are avoided with @JsonIgnoreProperties annotation.
   */
  @ManyToMany
  @JoinTable(name = "applied_jobs",
      joinColumns = @JoinColumn(name = "candidate_id"),
      inverseJoinColumns = @JoinColumn(name = "job_ad_id"))
  @JsonIgnoreProperties("candidates")
  // To prevent recursion when fetching JobAds that contain Candidates
  @Cascade(CascadeType.PERSIST)
  private List<JobAd> appliedJobAds = new ArrayList<>();

  /**
   * The candidate's CV file, stored as a byte array. Can be used to store the CV document in binary
   * format.
   */
  @Lob
  private byte[] cvFile;

  /**
   * The name of the uploaded cv file.
   */
  private String cvFileName;

  /**
   * Constructor for creating a new candidate.
   *
   * @param username the username of the candidate
   * @param password the password of the candidate
   * @param name     the first name of the candidate
   * @param lastname the last name of the candidate
   * @param email    the email address of the candidate
   */
  public Candidate(String username, String password, String name, String lastname, String email) {
    super(username, password, name, lastname, email, Role.CANDIDATE);
  }
}
