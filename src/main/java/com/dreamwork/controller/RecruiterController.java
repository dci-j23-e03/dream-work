package com.dreamwork.controller;

import com.dreamwork.model.user.Recruiter;
import com.dreamwork.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/recruiters")
public class RecruiterController {
  private final RecruiterService recruiterService;

  @Autowired
  public RecruiterController(RecruiterService recruiterService) {
    this.recruiterService = recruiterService;
  }

  @GetMapping("/recruiter_list")
  public String showAllRecruiters(Model model) {
    List<Recruiter> recruiters = recruiterService.getAllRecruiters();
    model.addAttribute("recruiters", recruiters);
    return "recruiter_list";
  }

  @GetMapping("/details/{userId}")
  public String showRecruiterDetails(@PathVariable Long userId, Model model) {
    Optional<Recruiter> recruiter = recruiterService.getRecruiterById(userId);
    if (recruiter.isPresent()) {
      model.addAttribute("recruiter", recruiter);
      return "recruiter_details";
    }
    return "redirect:/recruiters/list";
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

  @GetMapping("/update/{userId}")
  public String showUpdateForm(@PathVariable Long userId, Model model) {
    Optional<Recruiter> recruiter = recruiterService.getRecruiterById(userId);
    if (recruiter.isPresent()) {
      model.addAttribute("recruiter", recruiter.get());
      return "recruiter_update";
    }
    return "redirect:/recruiters/recruiter_list";
  }

  @PostMapping("/update/{userId}")
  public String updateRecruiter(@PathVariable Long userId, @ModelAttribute Recruiter recruiter) {
    recruiter.setUserId(userId);
    recruiterService.updateRecruiterById(userId, recruiter);
    return "redirect:/recruiters/recruiter_list";
  }

  @GetMapping("/delete/{userId}")
  public String deleteRecruiter(@PathVariable Long userId) {
    recruiterService.deleteRecruiter(userId);
    return "redirect:/recruiters/recruiter_list";
  }

}