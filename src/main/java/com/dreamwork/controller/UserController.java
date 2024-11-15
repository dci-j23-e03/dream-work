package com.dreamwork.controller;

import com.dreamwork.dto.UserDTO;
import com.dreamwork.model.job.Role;
import com.dreamwork.service.CandidateService;
import com.dreamwork.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final CandidateService candidateService;
  private final RecruiterService recruiterService;

  @Autowired
  public UserController(CandidateService candidateService, RecruiterService recruiterService) {
    this.candidateService = candidateService;
    this.recruiterService = recruiterService;
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody UserDTO user) {
    if (Role.CANDIDATE.name().equals(user.getRole())) {
      candidateService.saveCandidate(user);
    } else if (Role.RECRUITER.name().equals(user.getRole())) {
      recruiterService.saveRecruiter(user);
    }

    return ResponseEntity.ok("User registered successfully.");
  }
}
