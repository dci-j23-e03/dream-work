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

/**
 * Controller class for managing candidate-related operations, including updating candidate information,
 * applying to job ads, and viewing applied job ads.
 */
@Controller
@RequestMapping("/candidates")
public class CandidateController {

  private final CandidateService candidateService;
  private final JobAdService jobAdService;
  private final AuthenticationService authenticationService;

  /**
   * Constructor with required services.
   *
   * @param candidateService Service for managing Candidate
   * @param jobAdService Service for managing Job Ads
   * @param authenticationService Service for managing authentication and current user context.
   */
  @Autowired
  public CandidateController(CandidateService candidateService, JobAdService jobAdService,
      AuthenticationService authenticationService) {
    this.candidateService = candidateService;
    this.jobAdService = jobAdService;
    this.authenticationService = authenticationService;
  }

  /**
   * Displays the candidate's dashboard.
   *
   * @param model Spring's model to add attributes for the view.
   * @return View the Html file for the candidate dashboard.
   */
  @GetMapping
  public String getCandidateDashboard(Model model) {
    User user = authenticationService.getCurrentUser();
    model.addAttribute("candidate", user);

    return "candidate-dashboard";
  }

  /**
   * Displays the form to update candidate information.
   *
   * @param model            Spring's model to add attributes for the view.
   * @param updatedCandidate An empty candidate object for form binding.
   * @return View the Html file for updating candidate information.
   */
  @GetMapping("/update")
  public String getUpdateInfo(Model model, Candidate updatedCandidate) {
    Candidate candidate = (Candidate) authenticationService.getCurrentUser();
    model.addAttribute("candidate", candidate);
    model.addAttribute("updatedCandidate", updatedCandidate);
    return "candidate-update";
  }

  /**
   * Updates candidate information based on the provided data and current password.
   *
   * @param candidate        Candidate object containing updated details.
   * @param currentPassword  The current password for authentication.
   * @return Redirect URL to the candidate dashboard with a success flag.
   */
  @PostMapping("/update")
  public String updateCandidate(@ModelAttribute Candidate candidate,
      @RequestParam String currentPassword) {
    candidateService.updateCandidate(candidate, currentPassword);

    return "redirect:/candidates?successUpdate=true";
  }

  /**
   * Displays the form for applying to a specific job advertisement.
   *
   * @param model   Spring's model to add attributes for the view.
   * @param jobAdId The ID of the job ad to apply for.
   * @return View the Html file for applying to a job.
   */
  @GetMapping("/apply/job-ads/{jobAdId}")
  public String getApplyToJobForm(Model model, @PathVariable Long jobAdId) {

    model.addAttribute("jobAdId", jobAdId);
    model.addAttribute("candidate", authenticationService.getCurrentUser());

    return "candidate-apply";
  }

  /**
   * Processes the candidate's application to a job ad.
   *
   * @param model   Spring's model to add attributes for the view.
   * @param jobAdId The ID of the job ad to apply for.
   * @param cvFile  The candidate's CV file uploaded as part of the application.
   * @return Redirect URL to the dashboard with success message or job application form in case of an error.
   */
  @PostMapping("/apply/job-ads/{jobAdId}")
  public String applyToJob(Model model, @PathVariable Long jobAdId,
      @RequestParam MultipartFile cvFile) {

    // already applied to this job
    if (jobAdService.isJobAlreadyApplied(jobAdId)) {
      model.addAttribute("jobAdId", jobAdId);
      model.addAttribute("candidate", authenticationService.getCurrentUser());
      return "redirect:/job-ads/{jobAdId}?alreadyApplied=true";
    }
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

  /**
   * Displays a list of job ads the candidate has applied to.
   *
   * @param model Spring's model to add attributes for the view.
   * @return View Html file for listing applied job ads.
   */
  @GetMapping("/job-ads")
  public String getAllJobAdsForCandidate(Model model) {
    List<JobAdDTO> listAppliedJobs = jobAdService.getAllJobAdsForCandidate();
    model.addAttribute("listAppliedJobs", listAppliedJobs);
    return "candidate-list-applied-jobs";
  }
}
