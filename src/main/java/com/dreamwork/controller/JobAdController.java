package com.dreamwork.controller;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.model.job.Seniority;
import com.dreamwork.service.JobAdService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller class for handling job ads-related operations such as listing, filtering,
 * and viewing job ad details.
 */
@Controller
@RequestMapping("/job-ads")
public class JobAdController {

  private final JobAdService jobAdService;

  /**
   * Constructor  with required services.
   *
   * @param jobAdService          Service for managing job ad operations.
   * @param authenticationService Service for managing authentication and current user context.
   */
  @Autowired
  public JobAdController(JobAdService jobAdService) {
    this.jobAdService = jobAdService;
  }

  /**
   * Displays a paginated list of job ads.
   *
   * @param page  The page number to display, defaults to 0.
   * @param size  The number of items per page, defaults to 10.
   * @param model Spring's model to add attributes for the view.
   * @return View name for the list of job advertisements.
   */
  @GetMapping
  public String getAllJobAds(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      Model model) {
    Page<JobAdDTO> jobPage = jobAdService.getAllJobAds(page, size);

    long totalJobs = jobPage.getTotalElements();

    model.addAttribute("jobAds", jobPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", jobPage.getTotalPages());
    model.addAttribute("totalJobs", totalJobs);

    return "job-ads-list";
  }

  /**
   * Filters job ads based on criteria such as seniority, city, posted date and tech stack.
   *
   * @param seniority the seniority level to filter by (e.g., JUNIOR, MID_LEVEL, SENIOR); optional.
   * @param city the city to filter by; optional.
   * @param mainTechStack the main tech stack to filter by; optional.
   * @param date the date to filter jobs posted on or after; optional.
   * @param page the current page index for pagination; defaults to 0.
   * @param size the number of job ads per page; defaults to 10 if not specified.
   * @param model Spring's model to add attributes for the view.
   * @return View html file for the filtered list of job ads.
   */
  @GetMapping("/filter")
  public String getFilteredJobAds(
      @RequestParam(required = false) Seniority seniority,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String mainTechStack,
      @RequestParam(required = false, defaultValue = "1900-01-01") LocalDate date,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      Model model) {
    Page<JobAdDTO> jobPage = jobAdService.getFilteredJobAds(
        seniority, city, mainTechStack, date, page, size);

    long totalJobs = jobPage.getTotalElements();

    model.addAttribute("jobAds", jobPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", jobPage.getTotalPages());
    model.addAttribute("totalJobs", totalJobs);

    return "job-ads-list";
  }

  /**
   * Displays details for a specific job ad.
   *
   * @param model   Spring's model to add attributes for the view.
   * @param jobAdId The ID of the job ad to display.
   * @return View Html file for the job ad details.
   */
  @GetMapping("/{jobAdId}")
  public String getJobAdDetails(Model model, @PathVariable Long jobAdId) {
    JobAdDTO jobAdDTO = jobAdService.getJobAdById(jobAdId);
    model.addAttribute("job", jobAdDTO);
//    model.addAttribute(
//        "alreadyApplied", jobAdService.hasCandidateAlreadyApplied(jobAdId));
    return "job-details";
  }
}
