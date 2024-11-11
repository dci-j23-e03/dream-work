package com.dreamwork.model.user;

import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.job.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Recruiter extends User {

  private String companyName;

  @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL)
  private List<JobAd> jobAds;

  public Recruiter(String username, String password, String name, String lastname,
      String role, String companyName) {
    super(username, password, name, lastname, Role.RECRUITER);
    this.companyName = companyName;
  }

  public Recruiter() {}
}
