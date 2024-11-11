package com.dreamwork.controller;

import com.dreamwork.dto.CandidateDTO;
import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.dto.RecruiterDTO;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.service.JobAdService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job-ads")
public class JobAdController {

  private final JobAdService jobAdService;

  public JobAdController(@Autowired JobAdService jobAdService) {
    this.jobAdService = jobAdService;
  }

  @GetMapping
  public ResponseEntity<List<JobAdDTO>> getAllJobAds() {
    List<JobAd> jobAds = jobAdService.getAllJobAds();

    List<JobAdDTO> jobAdDTOs = jobAds.stream()
        .map(jobAd -> new JobAdDTO(
            jobAd.getPosition(),
            jobAd.getCountry(),
            jobAd.getCity(),
            jobAd.getSeniority().toString(),
            jobAd.getMainTechStack(),
            jobAd.getDescription(),
            new RecruiterDTO(
                jobAd.getRecruiter().getName(),
                jobAd.getRecruiter().getLastname(),
                jobAd.getRecruiter().getCompanyName()),
            jobAd.getCandidates().stream()
                .map(candidate -> new CandidateDTO(
                    candidate.getName(),
                    candidate.getLastname(),
                    candidate.getCountry()))
                .toList()
        ))
        .toList();

    return ResponseEntity.ok(jobAdDTOs);
  }

  @PostMapping
  public ResponseEntity<Object> createJobAd(@RequestBody JobAdDTO jobAdDto,
      @RequestParam Long recruiterId) {
    try {
      JobAd createdJobAd = jobAdService.createJobAd(jobAdDto, recruiterId);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdJobAd);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
    }
  }

  @PostMapping("/{jobAdId}/apply")
  public ResponseEntity<String> applyToJob(@PathVariable Long jobAdId,
      @RequestParam Long candidateId) {
    try {
      jobAdService.applyToJob(jobAdId, candidateId);
      return ResponseEntity.status(HttpStatus.CREATED).body("Applied successfully.");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
    }
  }
}
