package com.dreamwork.model.user;

import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.job.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Recruiter extends User {

  @Column(nullable = false)
  private String companyName;

  @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL)
  @JsonIgnore // To prevent recursion when fetching JobAds that contain Recruiters
  private List<JobAd> jobAds;

  public Recruiter(String username, String password, String name, String lastname,
      String companyName) {
    super(username, password, name, lastname, Role.RECRUITER);
    this.companyName = companyName;
  }

  public Recruiter() {
  }
}
