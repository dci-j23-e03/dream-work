package com.dreamwork.controller;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.dto.RecruiterDTO;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.repository.RecruiterRepository;
import com.dreamwork.service.RecruiterService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/recruiters")
public class RecruiterController {

  private final RecruiterService recruiterService;

  public RecruiterController(@Autowired RecruiterService recruiterService,
      RecruiterRepository recruiterRepository) {
    this.recruiterService = recruiterService;
  }

//  @PostMapping("/update")
//  public ResponseEntity<String> updateRecruiter(
//      @RequestBody Recruiter updatedRecruiter, @RequestParam String password) {
//    recruiterService.updateRecruiter(updatedRecruiter, password);
//    return ResponseEntity.ok("Recruiter updated successfully.");
//  }

  @GetMapping("/update")
  public String getUpdateInfo(Model model, Recruiter recruiter){
    model.addAttribute("recruiter", recruiter);
    return "recruiter-update";
  }

  @PostMapping("/update")
  private String updateRecruiter(@ModelAttribute Recruiter recruiter, @RequestParam String currentPassword){
    recruiterService.updateRecruiter(recruiter, currentPassword);
    return "recruiter-update";
  }

  @PostMapping("/delete")
  public ResponseEntity<Recruiter> deleteRecruiter(@RequestParam Long recruiterId) {
    recruiterService.deleteRecruiter(recruiterId);
    return ResponseEntity.noContent().build();
  }

//  @PostMapping("/delete")
//  public String deleteRecruiter(@RequestParam Long recruiterId,
//      RedirectAttributes redirectAttributes) {
//    recruiterService.deleteRecruiter(recruiterId);
//    redirectAttributes.addFlashAttribute("message", "User updated successfully.");
//
//    return "redirect:/";
//  }

  @GetMapping("/job-ads")
  public ResponseEntity<List<JobAdDTO>> getAllJobAdsByRecruiterId(@RequestParam Long recruiterId) {
    List<JobAdDTO> jobAdDTOs = recruiterService.getAllJobAdsByRecruiterId(recruiterId);
    return ResponseEntity.ok(jobAdDTOs);
  }
}