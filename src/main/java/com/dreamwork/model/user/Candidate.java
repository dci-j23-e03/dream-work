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

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Candidate extends User {

  @ManyToMany
  @JoinTable(name = "applied_jobs", joinColumns = @JoinColumn(name = "candidate_id"),
      inverseJoinColumns = @JoinColumn(name = "job_ad_id"))
  @JsonIgnoreProperties("candidates") // To prevent recursion when fetching JobAds that contain Candidates
  private List<JobAd> appliedJobAds = new ArrayList<>();

  @Lob
  private byte[] cvFile;

  private String cvFileName;

  public Candidate(String username, String password, String name, String lastname, String email) {
    super(username, password, name, lastname, email, Role.CANDIDATE);
  }
}
