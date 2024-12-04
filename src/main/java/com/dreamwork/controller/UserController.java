package com.dreamwork.controller;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.UserDTO;
import com.dreamwork.exception.IncorrectPasswordException;
import com.dreamwork.model.job.Role;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.service.CandidateService;
import com.dreamwork.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling user-related actions such as registration, login,
 * and account management. Supports operations for both candidates and recruiters.
 */
@Controller
@RequestMapping
public class UserController {

  private final CandidateService candidateService;
  private final RecruiterService recruiterService;
  private final AuthenticationService authenticationService;

  /**
   * Constructor with the specified services.
   *
   * @param candidateService the service for managing candidate operations.
   * @param recruiterService the service for managing recruiter operations.
   * @param authenticationService the service for authentication and user context.
   */
  @Autowired
  public UserController(CandidateService candidateService, RecruiterService recruiterService,
      AuthenticationService authenticationService) {
    this.candidateService = candidateService;
    this.recruiterService = recruiterService;
    this.authenticationService = authenticationService;
  }

  /**
   * Displays the registration page for new users.
   *
   * @param model the model to hold the user registration form data.
   * @return the name of the registration view.
   */
  @GetMapping("/register")
  public String register(Model model) {
    model.addAttribute("user", new UserDTO());
    return "register";
  }

  /**
   * Saves a new user after registration based on their role (Candidate or Recruiter).
   *
   * @param user the user details provided in the registration form.
   * @return a redirect to the login page with a success flag.
   */
  @PostMapping("/register")
  public String saveUser(@ModelAttribute UserDTO user) {
    if (Role.CANDIDATE.name().equals(user.getRole())) {
      candidateService.saveCandidate(user);
    } else if (Role.RECRUITER.name().equals(user.getRole())) {
      recruiterService.saveRecruiter(user);
    }

    return "redirect:/login?success";
  }

  /**
   * Displays the login page for users to authenticate.
   *
   * @return the name of the login view.
   */
  @GetMapping("/login")
  public String login() {
    return "login";
  }

  /**
   * Displays the account deletion confirmation page.
   *
   * @param model the model to hold the user data for the deletion form.
   * @return the name of the delete account view.
   */
  @GetMapping("/delete-account")
  public String deleteAccountPage(Model model) {
    model.addAttribute("user", new UserDTO());
    return "delete-account";
  }

  /**
   * Deletes the current user's account after validating the provided password.
   * Supports both candidates and recruiters based on the user type.
   *
   * @param password the user's password for verification.
   * @param model the model to hold any error messages in case of failure.
   * @param redirectAttributes attributes used to pass success messages after redirection.
   * @return a redirect to the job ads page if successful, or the delete account page on failure.
   */
  @PostMapping("/delete-account")
  public String deleteAccount(@RequestParam String password) {
    User currentUser = authenticationService.getCurrentUser();

    try {
      if (currentUser instanceof Candidate) {
        candidateService.deleteCandidate(password);
      } else if (currentUser instanceof Recruiter) {
        recruiterService.deleteRecruiter(password);
      }

      authenticationService.logout();

      return "redirect:/job-ads?success";

    } catch (IncorrectPasswordException e) {
      return "redirect:/delete-account?error";
    }
  }
}
