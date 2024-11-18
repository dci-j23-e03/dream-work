package com.dreamwork.controller;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.service.RecruiterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recruiters")
public class RecruiterController {

  private final RecruiterService recruiterService;

  public RecruiterController(@Autowired RecruiterService recruiterService) {
    this.recruiterService = recruiterService;
  }

  @PostMapping("/update")
  public ResponseEntity<String> updateRecruiter(@RequestParam Long recruiterId,
      @RequestBody Recruiter updatedRecruiter, @RequestParam String password) {
    recruiterService.updateRecruiter(recruiterId, updatedRecruiter, password);
    return ResponseEntity.ok("Recruiter updated successfully.");
  }

  @PostMapping("/delete")
  public ResponseEntity<Recruiter> deleteRecruiter(@RequestParam Long recruiterId) {
    recruiterService.deleteRecruiter(recruiterId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/job-ads")
  public ResponseEntity<List<JobAdDTO>> getAllJobAdsByRecruiterId(@RequestParam Long recruiterId) {
    List<JobAdDTO> jobAdDTOs = recruiterService.getAllJobAdsByRecruiterId(recruiterId);
    return ResponseEntity.ok(jobAdDTOs);
  }
}