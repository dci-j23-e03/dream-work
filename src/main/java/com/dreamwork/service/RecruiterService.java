package com.dreamwork.service;

import com.dreamwork.Repository.RecruiterRepository;
import com.dreamwork.model.job.Role;
import com.dreamwork.model.user.Recruiter;
import org.springframework.stereotype.Service;

@Service
public class RecruiterService {
  private final RecruiterRepository recruiterRepository;

  public RecruiterService(RecruiterRepository recruiterRepository) {
    this.recruiterRepository = recruiterRepository;
  }

  public void saveRecruiter(Recruiter recruiter) {
    if (recruiterRepository.findByUsername(recruiter.getUsername()) != null) {
      throw new IllegalArgumentException("Username already exists.");
    }
    recruiter.setRole(Role.RECRUITER);
    recruiterRepository.save(recruiter);
  }
}
