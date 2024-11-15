package com.dreamwork.service;

import com.dreamwork.dto.CandidateDTO;
import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.dto.UserDTO;
import com.dreamwork.exception.UserAlreadyExistsException;
import com.dreamwork.exception.UserNotFoundException;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.repository.RecruiterRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecruiterService {

  private final RecruiterRepository recruiterRepository;

  public RecruiterService(@Autowired RecruiterRepository recruiterRepository) {
    this.recruiterRepository = recruiterRepository;
  }

  @Transactional
  public void saveRecruiter(UserDTO user) {
    Optional<Recruiter> existingRecruiter = recruiterRepository.findByUsername(user.getUsername());
    if (existingRecruiter.isPresent()) {
      throw new UserAlreadyExistsException("Username already exists!");
    }

    recruiterRepository.save(new Recruiter(
        user.getUsername(),
        user.getPassword(),
        user.getName(),
        user.getLastname(),
        null));
  }

  // Needs proper implementation
  @Transactional
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

  @Transactional
  public void deleteRecruiter(Long recruiterId) {
    recruiterRepository.deleteById(recruiterId);
  }

  @Transactional(readOnly = true)
  public List<JobAdDTO> getAllJobAdsByRecruiterId(Long recruiterId) {
    Optional<Recruiter> recruiterOpt = recruiterRepository.findById(recruiterId);
    if (recruiterOpt.isEmpty()) {
      throw new UserNotFoundException("Recruiter does not exist!");
    }

    Recruiter recruiter = recruiterOpt.get();
    List<JobAd> jobAds = recruiter.getJobAds();

    return jobAds.stream()
        .map(jobAd -> new JobAdDTO(
            jobAd.getPosition(),
            jobAd.getCountry(),
            jobAd.getCity(),
            jobAd.getSeniority().toString(),
            jobAd.getMainTechStack(),
            jobAd.getDescription(),
            jobAd.getCandidates().stream()
                .map(candidate -> new CandidateDTO(
                    candidate.getName(),
                    candidate.getLastname(),
                    candidate.getCountry()
                )).toList()))
        .toList();
  }
}
