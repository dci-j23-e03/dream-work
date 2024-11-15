package com.dreamwork.service;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.dto.RecruiterDTO;
import com.dreamwork.dto.UserDTO;
import com.dreamwork.exception.UserAlreadyExistsException;
import com.dreamwork.exception.UserNotFoundException;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.repository.CandidateRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CandidateService {

  private final CandidateRepository candidateRepository;

  @Autowired
  public CandidateService(@Autowired CandidateRepository candidateRepository) {
    this.candidateRepository = candidateRepository;
  }

  @Transactional
  public void saveCandidate(UserDTO user) {
    Optional<Candidate> existingCandidate = candidateRepository
        .findByUsername(user.getUsername());
    if (existingCandidate.isPresent()) {
      throw new UserAlreadyExistsException("Username already exists!");
    }

    candidateRepository.save(new Candidate(
        user.getUsername(),
        user.getPassword(),
        user.getName(),
        user.getLastname(),
        null));
  }

  // Needs proper implementation
  @Transactional
  public Candidate updateCandidate(Long candidateId, Candidate updatedCandidate) {
    Candidate existingCandidate = candidateRepository.findById(candidateId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Candidate with id " + candidateId + " not found"));

    existingCandidate.setName(updatedCandidate.getName());
    existingCandidate.setLastname(updatedCandidate.getLastname());
    existingCandidate.setCountry(updatedCandidate.getCountry());

    return candidateRepository.save(existingCandidate);
  }

  @Transactional
  public void deleteCandidate(Long candidateId) {
    candidateRepository.deleteById(candidateId);
  }

  @Transactional(readOnly = true)
  public List<JobAdDTO> getAllJobAdsByCandidateId(Long candidateId) {
    Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);
    if (candidateOpt.isEmpty()) {
      throw new UserNotFoundException("Candidate does not exist!");
    }

    Candidate candidate = candidateOpt.get();
    List<JobAd> appliedJobAds = candidate.getAppliedJobAds();

    return appliedJobAds.stream()
        .map(jobAd -> new JobAdDTO(
            jobAd.getPosition(),
            jobAd.getCountry(),
            jobAd.getCity(),
            jobAd.getSeniority().toString(),
            jobAd.getMainTechStack(),
            jobAd.getDescription(),
            new RecruiterDTO(
                jobAd.getRecruiter().getName(),
                jobAd.getRecruiter().getLastname(),
                jobAd.getRecruiter().getCompanyName())
        ))
        .toList();
  }
}
