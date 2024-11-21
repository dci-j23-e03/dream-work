package com.dreamwork.controller;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.service.JobAdService;
import com.dreamwork.service.RecruiterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/recruiters")
public class RecruiterController {

  private final RecruiterService recruiterService;
  private final JobAdService jobAdService;
  private final AuthenticationService authenticationService;

  public RecruiterController(@Autowired RecruiterService recruiterService,
      JobAdService jobAdService, AuthenticationService authenticationService) {
    this.recruiterService = recruiterService;
    this.jobAdService = jobAdService;
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

  @PostMapping("/delete")
  public ResponseEntity<Recruiter> deleteRecruiter(@RequestParam Long recruiterId) {
    recruiterService.deleteRecruiter(recruiterId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/job-ads")
  public String getAllJobAdsForRecruiter(Model model) {
    List<JobAdDTO> jobAdDTOs = jobAdService.getAllJobAdsForRecruiter();
    model.addAttribute("jobAds", jobAdDTOs);

    return "recruiter-job-ads";
  }
}
