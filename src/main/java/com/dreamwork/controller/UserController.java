package com.dreamwork.controller;

import com.dreamwork.dto.UserDTO;
import com.dreamwork.exception.UserNotFoundException;
import com.dreamwork.model.job.Role;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.repository.CandidateRepository;
import com.dreamwork.repository.RecruiterRepository;
import com.dreamwork.service.CandidateService;
import com.dreamwork.service.RecruiterService;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final RecruiterRepository recruiterRepository;
  private final CandidateRepository candidateRepository;

  @Autowired
  public UserController(CandidateService candidateService, RecruiterService recruiterService,
      RecruiterRepository recruiterRepository, CandidateRepository candidateRepository) {
    this.candidateService = candidateService;
    this.recruiterService = recruiterService;
    this.recruiterRepository = recruiterRepository;
    this.candidateRepository = candidateRepository;
  }

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new UserDTO());
    return "register";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute("user") UserDTO user,
      RedirectAttributes redirectAttributes) {
    if (Role.CANDIDATE.name().equals(user.getRole())) {
      candidateService.saveCandidate(user);
    } else if (Role.RECRUITER.name().equals(user.getRole())) {
      recruiterService.saveRecruiter(user);
    }

    redirectAttributes.addFlashAttribute("message", "User registered successfully.");
    return "redirect:/login";
  }

  @GetMapping("/login")
  public String getLogin(Model model) {
    model.addAttribute("errorMessage", "Invalid username or password!");
    return "login";
  }

  @PostMapping("/login")
  public String login(@RequestParam String username, @RequestParam String password, Model model, RedirectAttributes redirectAttributes) {
    Optional<Recruiter> recruiter = recruiterRepository.findByUsername(username);
    Optional<Candidate> candidate = candidateRepository.findByUsername(username);
    if (recruiter.isPresent() && recruiter.get().getPassword().equals(password)) {
      model.addAttribute("recruiter", recruiter.get());
      model.addAttribute("jobAds", recruiterService.getAllJobAdsByRecruiterId(recruiter.get().getUserId()));
      return "recruiter_dashboard";
    } else if (candidate.isPresent() && candidate.get().getPassword().equals(password)) {
     model.addAttribute("candidate", candidate.get());
      return "candidate_dashboard";
    }
    return "login?error=true";
  }

//  @PostMapping("/register")
//  public String register(@ModelAttribute("user") UserDTO user, Model model) {
//    if ("CANDIDATE".equals(user.getRole())) {
//      candidateService.saveCandidate(user);
//    } else if ("RECRUITER".equals(user.getRole())) {
//      recruiterService.saveRecruiter(user);
//    }
//    model.addAttribute("message", "User registered successfully.");
//    return "register"; // Display the form with a success message, or redirect as needed
//  }
}
