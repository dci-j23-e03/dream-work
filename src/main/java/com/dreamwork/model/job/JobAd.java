package com.dreamwork.model.job;

import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class JobAd {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long jobAdId;

  private String position;
  private String country;
  private String city;

  @Enumerated(EnumType.STRING)
  private Seniority seniority;

  private String mainTechStack;
  private String description;

  @ManyToOne
  @JoinColumn(name = "recruiter_id", referencedColumnName = "recruiterId")
  private Recruiter recruiter;

  @ManyToMany(mappedBy = "appliedJobAds")
  private List<Candidate> candidates;

  public JobAd(String position, String country, String city, Seniority seniority,
      String mainTechStack, String description, Recruiter recruiter) {
    this.position = position;
    this.country = country;
    this.city = city;
    this.seniority = seniority;
    this.mainTechStack = mainTechStack;
    this.description = description;
    this.recruiter = recruiter;
  }

  public JobAd() {}
}
