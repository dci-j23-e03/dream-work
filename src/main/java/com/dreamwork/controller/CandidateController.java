package com.dreamwork.controller;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.User;
import com.dreamwork.service.CandidateService;
import com.dreamwork.service.JobAdService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/candidates")
public class CandidateController {

  private final CandidateService candidateService;
  private final JobAdService jobAdService;
  private final AuthenticationService authenticationService;

  @Autowired
  public CandidateController(CandidateService candidateService, JobAdService jobAdService,
      AuthenticationService authenticationService) {
    this.candidateService = candidateService;
    this.jobAdService = jobAdService;
    this.authenticationService = authenticationService;
  }

  @GetMapping
  public String getCandidateDashboard(Model model) {
    User user = authenticationService.getCurrentUser();
    model.addAttribute("candidate", user);

    return "candidate-dashboard";
  }

  @GetMapping("/update")
  public String getUpdateInfo(Model model, Candidate candidate) {
    model.addAttribute("candidate", candidate);
    return "candidate-update";
  }

  @PostMapping("/update")
  public String updateCandidate(@ModelAttribute Candidate candidate,
      @RequestParam String currentPassword) {
    candidateService.updateCandidate(candidate, currentPassword);

    return "candidate-update";
  }

  @PostMapping("/delete")
  public ResponseEntity<Candidate> deleteCandidate(@RequestParam Long candidateId) {
    candidateService.deleteCandidate(candidateId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/job-ads")
  public String getAllJobAdsForCandidate(Model model) {
    List<JobAdDTO> listAppliedJobs = jobAdService.getAllJobAdsForCandidate();
    model.addAttribute("listAppliedJobs", listAppliedJobs);
    return "candidate-list-applied-jobs";
  }

  @GetMapping("/view-cv/{candidateId}")
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
