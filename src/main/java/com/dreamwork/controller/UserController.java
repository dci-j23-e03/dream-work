package com.dreamwork.controller;

import com.dreamwork.dto.UserDTO;
import com.dreamwork.model.job.Role;
import com.dreamwork.service.CandidateService;
import com.dreamwork.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class UserController {

  private final CandidateService candidateService;
  private final RecruiterService recruiterService;


  @Autowired
  public UserController(CandidateService candidateService, RecruiterService recruiterService) {
    this.candidateService = candidateService;
    this.recruiterService = recruiterService;
  }

  @GetMapping("/register")
  public String register(Model model) {
    model.addAttribute("user", new UserDTO());
    return "register";
  }

  @PostMapping("/register")
  public String saveUser(@ModelAttribute("user") UserDTO user,RedirectAttributes redirectAttributes) {
    if (Role.CANDIDATE.name().equals(user.getRole())) {
      candidateService.saveCandidate(user);
    } else if (Role.RECRUITER.name().equals(user.getRole())) {
      recruiterService.saveRecruiter(user);
    }
    return "redirect:/login:/login?success";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

}
