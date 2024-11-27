package com.dreamwork.controller;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.service.JobAdService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

  @GetMapping
  public String getAllJobAds(Model model) {
    List<JobAdDTO> jobAdDTOs = jobAdService.getAllJobAds();
    model.addAttribute("jobAds", jobAdDTOs);

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
}
