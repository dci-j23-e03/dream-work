package com.dreamwork.controller;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.model.job.Seniority;
import com.dreamwork.service.JobAdService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/job-ads")
public class JobAdController {

  private final JobAdService jobAdService;
  private final AuthenticationService authenticationService;

  @Autowired
  public JobAdController(JobAdService jobAdService, AuthenticationService authenticationService) {
    this.jobAdService = jobAdService;
    this.authenticationService = authenticationService;
  }

  @GetMapping
  public String listJobs(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      Model model) {
    Page<JobAdDTO> jobPage = jobAdService.getJobs(page, size);

    long totalJobs = jobPage.getTotalElements();

    model.addAttribute("jobAds", jobPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", jobPage.getTotalPages());
    model.addAttribute("totalJobs", totalJobs);

    return "job-ads-list";
  }

  @GetMapping("/filter")
  public String getFilteredJobAds(
      @RequestParam(required = false) Seniority seniority,
      @RequestParam(required = false) String city,
//      @RequestParam(required = false, defaultValue = "anytime") String datePosted,
      @RequestParam(required = false) String techStack,
      Model model) {
    List<JobAdDTO> filteredJobAds = jobAdService.getFilteredJobAds(seniority, city, techStack); //, datePosted
    model.addAttribute("jobAds", filteredJobAds);
    return "job-ads-list";
  }

  @GetMapping("/{jobAdId}")
  public String getJobAdDetails(Model model, @PathVariable Long jobAdId) {
    JobAdDTO jobAdDTO = jobAdService.getJobAdById(jobAdId);
    model.addAttribute("job", jobAdDTO);
    model.addAttribute("alreadyApplied", jobAdService.isJobAlreadyApplied(jobAdId));
    return "job-details";
  }
}
