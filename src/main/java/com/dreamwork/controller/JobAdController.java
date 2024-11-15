package com.dreamwork.controller;

import com.dreamwork.dto.JobAdDTO;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/job-ads")
public class JobAdController {

  private final JobAdService jobAdService;

  public JobAdController(@Autowired JobAdService jobAdService) {
    this.jobAdService = jobAdService;
  }

  @GetMapping
  public ResponseEntity<List<JobAdDTO>> getAllJobAds() {
    List<JobAdDTO> jobAdDTOs = jobAdService.getAllJobAds();
    return ResponseEntity.ok(jobAdDTOs);
  }

  @PostMapping
  public ResponseEntity<JobAd> createJobAd(@RequestBody JobAd jobAd,
      @RequestParam Long recruiterId) {
    jobAdService.createJobAd(jobAd, recruiterId);
    return ResponseEntity.status(HttpStatus.CREATED).body(jobAd);
  }

  @PostMapping("/{jobAdId}/apply")
  public ResponseEntity<String> applyToJob(@PathVariable Long jobAdId,
      @RequestParam Long candidateId, @RequestParam MultipartFile cvFile) {
    jobAdService.applyToJob(jobAdId, candidateId, cvFile);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body("Applied successfully.");
  }
}
