package com.dreamwork.controller;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.service.JobAdService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/job-ads")
public class JobAdController {

  private final JobAdService jobAdService;

  @Autowired
  public JobAdController(JobAdService jobAdService) {
    this.jobAdService = jobAdService;
  }

  //figure out how to get the job ads to display on the landing page

//  @GetMapping("/")
//  public String landingPage(Model model) {
//    List<JobAdDTO> jobAdDTOs = jobAdService.getAllJobAds();
//    model.addAttribute("jobAds", jobAdDTOs);
//    return "job-ads-list";
//  }

//  @GetMapping("/job-ads-list")
//  public String getAllJobAds(Model model) {
//    List<JobAdDTO> jobAdDTOs = jobAdService.getAllJobAds();
//    model.addAttribute("jobAds", jobAdDTOs);
//
//    return "job-ads-list";
//  }

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

//  @GetMapping("/...")
//  public String getAllJobAds(@RequestParam(required = false) String seniority,
//      @RequestParam(required = false) String city,
//      @RequestParam(required = false) String datePosted,
//      @RequestParam(required = false) String techStack,
//      Model model) {
//    List<JobAdDTO> jobAdDTOs = jobAdService.getAllJobAds(seniority, city, datePosted, techStack);
//    model.addAttribute("jobAds", jobAdDTOs);
//
//    return "index";
//  }

  @GetMapping("/{jobAdId}")
  public String getJobAdDetails(Model model, @PathVariable Long jobAdId) {
    JobAdDTO jobAdDTO = jobAdService.getJobAdById(jobAdId);
    model.addAttribute("job", jobAdDTO);

    return "job-details";
  }

  @GetMapping("create")
  public String getCreateJobAdForm(Model model) {
    model.addAttribute("jobAd", new JobAd());

    return "create-job-ad";
  }

  @PostMapping("create")
  public String createJobAd(JobAd jobAd) {
    jobAdService.createJobAd(jobAd);

    return "redirect:/recruiters?successCreate=true";
  }

  @GetMapping("/apply/{jobAdId}")
  public String getApplyToJobForm(Model model, @PathVariable Long jobAdId) {
    model.addAttribute("jobAdId", jobAdId);
    model.addAttribute("candidate", authenticationService.getCurrentUser());

    return "candidate-apply";
  }

  @PostMapping("/apply/{jobAdId}")
  public String applyToJob(Model model, @PathVariable Long jobAdId,
      @RequestParam MultipartFile cvFile) {
    try {
      jobAdService.applyToJob(jobAdId, cvFile);
      return "redirect:/candidates?successApply=true";
    } catch (JobAdNotFoundException | CvFileSaveException e) {
      model.addAttribute("errorMessage", e.getMessage());
      model.addAttribute("jobAdId", jobAdId);
      model.addAttribute("candidate", authenticationService.getCurrentUser());
      return "candidate-apply";
    }
  }

  @GetMapping("/{jobAdId}/candidates")
  public String getAllCandidatesForJobAd(Model model, @PathVariable Long jobAdId) {
    List<CandidateDTO> candidateDTOs = candidateService.getAllCandidatesForJobAd(jobAdId);
    model.addAttribute("candidatesList", candidateDTOs);

    return "job-ad-candidates";
  }


}
