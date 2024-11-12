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
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class JobAd {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long jobAdId;

  @Column(nullable = false)
  private String position;

  @Column(nullable = false)
  private String country;

  @Column(nullable = false)
  private String city;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Seniority seniority;

  @Column(nullable = false)
  private String mainTechStack;

  @Column(nullable = false, length = 2000)
  private String description;

  @ManyToOne
  @JoinColumn(name = "recruiter_id", referencedColumnName = "userId")
  @JsonIgnoreProperties("jobAds") // To avoid circular references with Recruiter
  private Recruiter recruiter;

  @ManyToMany(mappedBy = "appliedJobAds")
  @JsonIgnoreProperties("appliedJobAds") // To avoid circular references with Candidate
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

  public JobAd() {
  }
}
