package com.dreamwork.model.user;

import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.job.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Represents a recruiter user in the system.
 * <p>
 * A recruiter can post multiple job advertisements. This entity extends the User class to inherit
 * common user attributes.
 * <p>
 * It uses lombok annotations to generate getters, setters and a no-argument constructor.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Recruiter extends User {

  /**
   * A list of job ads created by the recruiter.
   * <p>
   * This is a one-to-many relationship with JobAd. Cascade operations are enabled to propagate
   * changes from the recruiter to their associated job ads. Circular references are avoided with
   * JsonIgnore annotation.
   */
  @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<JobAd> jobAds = new ArrayList<>();

  /**
   * Constructor for creating a new recruiter.
   *
   * @param username the username of the recruiter
   * @param password the password of the recruiter
   * @param name     the first name of the recruiter
   * @param lastname the last name of the recruiter
   * @param email    the email address of the recruiter
   */
  public Recruiter(String username, String password, String name, String lastname, String email) {
    super(username, password, name, lastname, email, Role.RECRUITER);
  }
}
