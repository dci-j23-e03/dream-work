package com.dreamwork.service;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.dto.RecruiterDTO;
import com.dreamwork.dto.UserDTO;
import com.dreamwork.exception.CvFileNotFoundException;
import com.dreamwork.exception.IncorrectPasswordException;
import com.dreamwork.exception.UserAlreadyExistsException;
import com.dreamwork.exception.UserNotFoundException;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.User;
import com.dreamwork.repository.CandidateRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class CandidateService {

  private final CandidateRepository candidateRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationService authenticationService;

  @Autowired
  public CandidateService(@Autowired CandidateRepository candidateRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationService authenticationService) {
    this.candidateRepository = candidateRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationService = authenticationService;
  }

  public Candidate getCandidateByUsername(String username) {
    return candidateRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("Candidate does not exist!"));
  }

  @Transactional
  public void saveCandidate(UserDTO user) {
    Optional<Candidate> candidateOpt = candidateRepository
        .findByUsername(user.getUsername());
    if (candidateOpt.isPresent()) {
      throw new UserAlreadyExistsException("Username already exists!");
    }

    candidateRepository.save(new Candidate(
        user.getUsername(),
        passwordEncoder.encode(user.getPassword()),
        user.getName(),
        user.getLastname(),
        null));
  }

  @Transactional
  public Candidate updateCandidate(@RequestBody Candidate updatedCandidate, @RequestParam String password) {
    User user = authenticationService.getCurrentUser();
    Candidate candidate = (Candidate) user;

    if (!passwordEncoder.matches(password, candidate.getPassword())) {
      throw new IncorrectPasswordException("Incorrect password!");
    }

    candidate.setPassword(passwordEncoder.encode(updatedCandidate.getPassword()));
    candidate.setName(updatedCandidate.getName());
    candidate.setLastname(updatedCandidate.getLastname());
    candidate.setCountry(updatedCandidate.getCountry());
    candidateRepository.save(candidate);

    return updatedCandidate;
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

  @Transactional
  public Candidate viewCv(Long candidateId) {
    Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);

    if (candidateOpt.isEmpty()) {
      throw new UserNotFoundException("Candidate does not exist!");
    }

    Candidate candidate = candidateOpt.get();

    if (candidate.getCvFile() == null || candidate.getCvFileName() == null) {
      throw new CvFileNotFoundException("CV file not found!");
    }

    return candidate;
  }
}
