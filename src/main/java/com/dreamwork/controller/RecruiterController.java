package com.dreamwork.controller;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.CandidateDTO;
import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.exception.IncorrectPasswordException;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.service.CandidateService;
import com.dreamwork.service.JobAdService;
import com.dreamwork.service.RecruiterService;
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

/**
 * Controller for handling recruiter-related operations. Provides endpoints for recruiters to manage
 * their profile, job ads, and view candidate applications. Includes functionality for updating
 * recruiter details, creating and deleting job ads, and viewing candidate CVs.
 */
@Controller
@RequestMapping("/recruiters")
public class RecruiterController {

  private final RecruiterService recruiterService;
  private final JobAdService jobAdService;
  private final CandidateService candidateService;
  private final AuthenticationService authenticationService;

  /**
   * Constructor for required services
   *
   * @param recruiterService the service for managing recruiter operations.
   * @param jobAdService the service for managing job ad operations.
   * @param candidateService the service for managing candidate operations.
   * @param authenticationService the service for authentication and user context.
   */
  @Autowired
  public RecruiterController(RecruiterService recruiterService, JobAdService jobAdService,
      CandidateService candidateService, AuthenticationService authenticationService) {
    this.recruiterService = recruiterService;
    this.jobAdService = jobAdService;
    this.candidateService = candidateService;
    this.authenticationService = authenticationService;
  }

  /**
   * Displays the recruiter's dashboard.
   *
   * @param model Spring's model to add attributes for the view.
   * @return View the Html file for the recruiter dashboard.
   */
  @GetMapping
  public String getRecruiterDashboard(Model model) {
    User user = authenticationService.getCurrentUser();
    model.addAttribute("recruiter", user);

    return "recruiter-dashboard";
  }

  /**
   * Displays the form for updating recruiter information.
   *
   * @param model            Spring's model to add attributes for the view.
   * @param updatedRecruiter a placeholder for the updated recruiter information.
   * @return the name of the update recruiter information view.
   */
  @GetMapping("/update")
  public String getUpdateInfo(Model model, Recruiter updatedRecruiter) {
    Recruiter recruiter = (Recruiter) authenticationService.getCurrentUser();
    model.addAttribute("recruiter", recruiter);
    model.addAttribute("updatedRecruiter", updatedRecruiter);
    return "recruiter-update";
  }

  /**
   * Updates the recruiter details.
   *
   * @param recruiter       the updated recruiter data.
   * @param currentPassword the current password for verification.
   * @return a redirect to the recruiter dashboard with a success flag.
   */
  @PostMapping("/update")
  public String updateRecruiter(@ModelAttribute Recruiter recruiter,
      @RequestParam String currentPassword) {
    try {
      recruiterService.updateRecruiter(recruiter, currentPassword);
    } catch (IncorrectPasswordException e) {
      return "redirect:/recruiters/update?error";
    }

    return "redirect:/recruiters?update";
  }

  /**
   * Displays the form for creating a new job ad.
   *
   * @param model Spring's model to add attributes for the view.
   * @return the name of the create job ad view.
   */
  @GetMapping("/job-ads/create")
  public String getCreateJobAdForm(Model model) {
    model.addAttribute("jobAd", new JobAd());

    return "create-job-ad";
  }

  /**
   * Creates a new job ad.
   *
   * @param jobAd the job ad to create.
   * @return a redirect to the recruiter dashboard with a success flag.
   */
  @PostMapping("/job-ads/create")
  public String createJobAd(JobAd jobAd) {
    jobAdService.createJobAd(jobAd);

    return "redirect:/recruiters?create";
  }

  /**
   * Deletes a job ad by its ID.
   *
   * @param id the ID of the job ad to delete.
   * @return a response entity redirecting to the job ads list.
   */
  @PostMapping("/job-ads/delete/{id}")
  public ResponseEntity<Void> deleteJobAd(@PathVariable Long id) {
    jobAdService.deleteJobAd(id);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/recruiters/job-ads");
    return new ResponseEntity<>(headers, HttpStatus.FOUND); // HTTP 302: Redirect
  }

  /**
   * Retrieves all job ad created by the recruiter.
   *
   * @param model  Spring's model to add attributes for the view.
   * @return the name of the recruiter job ads list view.
   */
  @GetMapping("/job-ads")
  public String getAllJobAdsForRecruiter(Model model) {
    List<JobAdDTO> jobAdDTOs = jobAdService.getAllJobAdsForRecruiter();
    model.addAttribute("jobAds", jobAdDTOs);

    return "recruiter-job-ads";
  }

  /**
   * Retrieves the details of a specific job ad by its ID as a recruiter.
   *
   * @param id    the ID of the job ad.
   * @param model  Spring's model to add attributes for the view.
   * @return the name of the job ad details view.
   */
  @GetMapping("/job-ads/{id}")
  public String getJobAdDetails(@PathVariable Long id, Model model) {
    JobAdDTO jobAd = jobAdService.getJobAdById(id);
    model.addAttribute("jobAd", jobAd);

    List<CandidateDTO> candidates = candidateService.getAllCandidatesForJobAd(id);
    model.addAttribute("candidates", candidates);
    return "/recruiter-job-description";
  }

  /**
   * Provides the CV of a candidate for viewing.
   *
   * @param candidateId the ID of the candidate whose CV is being viewed.
   * @return a response entity with the CV file as an PDF.
   */
  @GetMapping("/job-ads/view-cv/candidate/{candidateId}")
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
