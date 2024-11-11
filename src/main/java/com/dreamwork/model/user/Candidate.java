package com.dreamwork.model.user;

import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.job.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Candidate extends User {

  private String country;

  @ManyToMany
  @JoinTable(name = "applied_jobs", joinColumns = @JoinColumn(name = "candidate_id"),
      inverseJoinColumns = @JoinColumn(name = "job_ad_id"))
  @JsonManagedReference
  private List<JobAd> appliedJobAds;

  public Candidate(String username, String password, String name, String lastname, String country) {
    super(username, password, name, lastname, Role.CANDIDATE);
    this.country = country;
  }

  public Candidate() {
  }
}
