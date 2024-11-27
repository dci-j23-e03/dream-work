package com.dreamwork.controller;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.UserDTO;
import com.dreamwork.model.job.Role;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.service.CandidateService;
import com.dreamwork.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class UserController {

  private final CandidateService candidateService;
  private final RecruiterService recruiterService;
  private final AuthenticationService authenticationService;

  @Autowired
  public UserController(CandidateService candidateService, RecruiterService recruiterService,
                        AuthenticationService authenticationService) {
    this.candidateService = candidateService;
    this.recruiterService = recruiterService;
    this.authenticationService = authenticationService;
  }

  @GetMapping("/register")
  public String register(Model model) {
    model.addAttribute("user", new UserDTO());
    return "register";
  }

  @PostMapping("/register")
  public String saveUser(@ModelAttribute UserDTO user) {
    if (Role.CANDIDATE.name().equals(user.getRole())) {
      candidateService.saveCandidate(user);
    } else if (Role.RECRUITER.name().equals(user.getRole())) {
      recruiterService.saveRecruiter(user);
    }

    return "redirect:/login?success";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }


  @GetMapping("/delete-account")
  public String deleteAccountPage(Model model) {
    model.addAttribute("user", new UserDTO());
    return "delete-account";
  }


  @PostMapping("/delete-account")
  public String deleteAccount(@RequestParam String password,
                              Model model, RedirectAttributes redirectAttributes) {

    User currentUser = authenticationService.getCurrentUser();
    boolean isDeleted = false;

    if (currentUser instanceof Candidate) {
      isDeleted = candidateService.deleteCandidate(password);
    } else if (currentUser instanceof Recruiter) {
      isDeleted = recruiterService.deleteRecruiter(password);
    }

    if (isDeleted) {
      authenticationService.logout();
      redirectAttributes.addFlashAttribute("successMessage", "Account successfully deleted.");
      return "redirect:/job-ads/job-ads-list?success";
    } else {
      model.addAttribute("errorMessage", "Failed to delete account. Please try again.");
      return "delete-account";
    }
  }

}
