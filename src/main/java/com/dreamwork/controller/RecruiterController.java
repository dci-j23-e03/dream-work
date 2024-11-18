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
@RequestMapping("/recruiter")
public class RecruiterController {

  private final RecruiterService recruiterService;

  public RecruiterController(@Autowired RecruiterService recruiterService,
      RecruiterRepository recruiterRepository) {
    this.recruiterService = recruiterService;
  }

  @PostMapping("/update")
  public ResponseEntity<String> updateRecruiter(@RequestParam Long recruiterId,
      @RequestBody Recruiter updatedRecruiter, @RequestParam String password) {
    recruiterService.updateRecruiter(recruiterId, updatedRecruiter, password);
    return ResponseEntity.ok("Recruiter updated successfully.");
  }

  @GetMapping("/recruiter_update")
  public String showRecUpdateForm(Model model) {
    model.addAttribute("recruiter", new RecruiterDTO());
    return "recruiter_update";
  }

  @PostMapping("/recruiter_update")
  public String updateRecruiter(@ModelAttribute("recruiter") Recruiter recruiter,
      RedirectAttributes redirectAttributes, HttpSession session) {
    User user = (User) session.getAttribute("user");
    Long userId = user.getUserId();
    recruiterService.updateRecruiter(userId, recruiter, recruiter.getPassword());
    redirectAttributes.addFlashAttribute("message", "User updated successfully.");
    return "redirect:/recruiter_dashboard";
  }

  @PostMapping("/delete")
  public ResponseEntity<Recruiter> deleteRecruiter(@RequestParam Long recruiterId) {
    recruiterService.deleteRecruiter(recruiterId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/delete")
  public String deleteRecruiter(@RequestParam Long recruiterId,
      RedirectAttributes redirectAttributes) {
    recruiterService.deleteRecruiter(recruiterId);
    redirectAttributes.addFlashAttribute("message", "User updated successfully.");

    return "redirect:/";
  }

  @GetMapping("/job-ads")
  public ResponseEntity<List<JobAdDTO>> getAllJobAdsByRecruiterId(@RequestParam Long recruiterId) {
    List<JobAdDTO> jobAdDTOs = recruiterService.getAllJobAdsByRecruiterId(recruiterId);
    return ResponseEntity.ok(jobAdDTOs);
  }
}