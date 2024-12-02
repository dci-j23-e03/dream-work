package com.dreamwork.controller;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.CandidateDTO;
import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.service.CandidateService;
import com.dreamwork.service.JobAdService;
import com.dreamwork.service.RecruiterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/recruiters")
public class RecruiterController {

  private final RecruiterService recruiterService;
  private final JobAdService jobAdService;
  private final CandidateService candidateService;

  private final AuthenticationService authenticationService;

  @Autowired
  public RecruiterController(RecruiterService recruiterService, JobAdService jobAdService,
      CandidateService candidateService,
      AuthenticationService authenticationService) {
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

    return "redirect:/recruiters?successUpdate=true";
  }

  @PostMapping("/delete")
  public ResponseEntity<Recruiter> deleteRecruiter(@RequestParam String password ) {
    recruiterService.deleteRecruiter(password);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/job-ads")
  public String getAllJobAdsForRecruiter(Model model) {
    List<JobAdDTO> jobAdDTOs = jobAdService.getAllJobAdsForRecruiter();
    model.addAttribute("jobAds", jobAdDTOs);

    return "recruiter-job-ads";
  }

  @GetMapping("/job-ads/{id}")
  public String getJobDetails(@PathVariable Long id, Model model) {
    JobAdDTO jobAd = jobAdService.getJobAdById(id);
    model.addAttribute("jobAd", jobAd);

    List<CandidateDTO> candidates = candidateService.getAllCandidatesForJobAd(id);
    model.addAttribute("candidates", candidates);
    return "/recruiter-job-description";
  }
}
