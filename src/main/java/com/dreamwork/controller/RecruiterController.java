package com.dreamwork.controller;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.CandidateDTO;
import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.exception.JobAdNotFoundException;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.service.CandidateService;
import com.dreamwork.service.JobAdService;
import com.dreamwork.service.RecruiterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/recruiters")
public class RecruiterController {

  private final RecruiterService recruiterService;
  private final JobAdService jobAdService;
  private final CandidateService candidateService;
  private final AuthenticationService authenticationService;

  @Autowired
  public RecruiterController(RecruiterService recruiterService, JobAdService jobAdService,
      CandidateService candidateService, AuthenticationService authenticationService) {
    this.recruiterService = recruiterService;
    this.jobAdService = jobAdService;
    this.candidateService = candidateService;
    this.authenticationService = authenticationService;
  }


  @GetMapping
  public String getRecruiterDashboard(Model model) {
    User user = authenticationService.getCurrentUser();
    model.addAttribute("recruiter", user);

    return "recruiter-dashboard";
  }


  @GetMapping("/update")
  public String getUpdateInfo(Model model, Recruiter recruiter) {
    model.addAttribute("recruiter", recruiter);
    return "recruiter-update";
  }


  @PostMapping("/update")
  public String updateRecruiter(@ModelAttribute Recruiter recruiter,
      @RequestParam String currentPassword) {
    recruiterService.updateRecruiter(recruiter, currentPassword);

    return "recruiter-update";
  }


  @GetMapping("/job-ads/create")
  public String getCreateJobAdForm(Model model) {
    model.addAttribute("jobAd", new JobAd());

    return "create-job-ad";
  }


  @PostMapping("/job-ads/create")
  public String createJobAd(JobAd jobAd) {
    jobAdService.createJobAd(jobAd);

    return "redirect:/recruiters";
  }


  @PostMapping("/job-ads/delete/{id}")
  public ResponseEntity<Void> deleteJobAd(@PathVariable Long id) {
    jobAdService.deleteJobAdById(id);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/recruiters/job-ads");
    return new ResponseEntity<>(headers, HttpStatus.FOUND); // HTTP 302: Redirect
  }


  @GetMapping("/job-ads")
  public String getAllJobAdsForRecruiter(Model model) {
    List<JobAdDTO> jobAdDTOs = jobAdService.getAllJobAdsForRecruiter();
    model.addAttribute("jobAds", jobAdDTOs);

    return "recruiter-job-ads";
  }


  @GetMapping("/job-ads/{id}")
  public String getJobAdDetails(@PathVariable Long id, Model model) {
    JobAdDTO jobAd = jobAdService.getJobAdById(id);
    model.addAttribute("jobAd", jobAd);

    List<CandidateDTO> candidates = candidateService.getAllCandidatesForJobAd(id);
    model.addAttribute("candidates", candidates);
    return "/recruiter-job-description";
  }


  @GetMapping("/job-ads/view-cv/candidate/{candidateId}")
  public ResponseEntity<byte[]> viewCv(@PathVariable Long candidateId) {
    Candidate candidate = candidateService.viewCv(candidateId);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition
        .inline()
        .filename(candidate.getCvFileName())
        .build());

    return ResponseEntity.status(HttpStatus.OK).headers(headers).body(candidate.getCvFile());
  }
}
