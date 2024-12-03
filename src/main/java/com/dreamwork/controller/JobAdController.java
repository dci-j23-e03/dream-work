package com.dreamwork.controller;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.service.JobAdService;
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

  @Autowired
  public JobAdController(JobAdService jobAdService) {
    this.jobAdService = jobAdService;
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

  //  @GetMapping("/job-ads-list")
//  public String getAllJobAds(@RequestParam(required = false) String seniority,
//                             @RequestParam(required = false) String city,
//                             @RequestParam(required = false) String datePosted,
//                             @RequestParam(required = false) String techStack,
//                             Model model) {
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
}
