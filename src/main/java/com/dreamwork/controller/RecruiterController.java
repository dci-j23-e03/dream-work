package com.dreamwork.controller;

import com.dreamwork.model.user.Recruiter;
import com.dreamwork.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recruiters")
public class RecruiterController {
  private final RecruiterService recruiterService;

  @Autowired
  public RecruiterController(RecruiterService recruiterService) {
    this.recruiterService = recruiterService;
  }

  @GetMapping("/register")
  public String showRecruiterRegistrationForm(Model model) {
    model.addAttribute("recruiter", new Recruiter());
    return "recruiter_register";
  }

  @PostMapping("/register")
  public String registerRecruiter(Recruiter recruiter) {
    recruiterService.saveRecruiter(recruiter);
    return "success";
  }

}