package com.dreamwork.controller;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.exception.CvFileSaveException;
import com.dreamwork.exception.JobAdNotFoundException;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.User;
import com.dreamwork.service.CandidateService;
import com.dreamwork.service.JobAdService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    return "redirect:/candidates?successUpdate=true";
  }

  @GetMapping("/apply/job-ads/{jobAdId}")
  public String getApplyToJobForm(Model model, @PathVariable Long jobAdId) {
    model.addAttribute("jobAdId", jobAdId);
    model.addAttribute("candidate", authenticationService.getCurrentUser());

    return "candidate-apply";
  }

  @PostMapping("/apply/job-ads/{jobAdId}")
  public String applyToJob(Model model, @PathVariable Long jobAdId,
      @RequestParam MultipartFile cvFile) {
    try {
      jobAdService.applyToJob(jobAdId, cvFile);
      return "redirect:/candidates";
    } catch (JobAdNotFoundException | CvFileSaveException e) {
      model.addAttribute("errorMessage", e.getMessage());
      model.addAttribute("jobAdId", jobAdId);
      model.addAttribute("candidate", authenticationService.getCurrentUser());
      return "candidate-apply";
    }
  }

  @GetMapping("/job-ads")
  public String getAllJobAdsForCandidate(Model model) {
    List<JobAdDTO> listAppliedJobs = jobAdService.getAllJobAdsForCandidate();
    model.addAttribute("listAppliedJobs", listAppliedJobs);
    return "candidate-list-applied-jobs";
  }
}
