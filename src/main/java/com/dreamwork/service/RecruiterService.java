package com.dreamwork.service;

import com.dreamwork.repository.RecruiterRepository;
import com.dreamwork.model.job.Role;
import com.dreamwork.model.user.Recruiter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecruiterService {
  private final RecruiterRepository recruiterRepository;

  public RecruiterService(RecruiterRepository recruiterRepository) {
    this.recruiterRepository = recruiterRepository;
  }

  public List<Recruiter> getAllRecruiters() {
    return recruiterRepository.findAll();
  }

  public Optional<Recruiter> getRecruiterById(Long id) {
    return recruiterRepository.findById(id);
  }

  public void saveRecruiter(Recruiter recruiter) {
    if (recruiterRepository.findByUsername(recruiter.getUsername()) != null) {
      throw new IllegalArgumentException("Username already exists.");
    }
    recruiter.setRole(Role.RECRUITER);
    recruiterRepository.save(recruiter);
  }

  public void updateRecruiterById(Long userId, Recruiter updatedRecruiter) {
    Optional<Recruiter> existingRecruiterOptional = recruiterRepository.findById(userId);
    if (existingRecruiterOptional.isPresent()) {
      Recruiter existingRecruiter = existingRecruiterOptional.get();
      existingRecruiter.setUsername(updatedRecruiter.getUsername());
      existingRecruiter.setPassword(updatedRecruiter.getPassword());
      existingRecruiter.setName(updatedRecruiter.getName());
      existingRecruiter.setLastname(updatedRecruiter.getLastname());
      existingRecruiter.setCompanyName(updatedRecruiter.getCompanyName());
    }
  }

  public void deleteRecruiter(Long id) {
    recruiterRepository.deleteById(id);
  }
}
