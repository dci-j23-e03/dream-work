package com.dreamwork.service;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.CandidateDTO;
import com.dreamwork.dto.UserDTO;
import com.dreamwork.exception.CvFileNotFoundException;
import com.dreamwork.exception.IncorrectPasswordException;
import com.dreamwork.exception.JobAdNotFoundException;
import com.dreamwork.exception.UserAlreadyExistsException;
import com.dreamwork.exception.UserNotFoundException;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.User;
import com.dreamwork.repository.CandidateRepository;
import com.dreamwork.repository.JobAdRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CandidateService {

  private final CandidateRepository candidateRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationService authenticationService;
  private final JobAdRepository jobAdRepository;

  @Autowired
  public CandidateService(CandidateRepository candidateRepository, PasswordEncoder passwordEncoder,
      AuthenticationService authenticationService, JobAdRepository jobAdRepository) {
    this.candidateRepository = candidateRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationService = authenticationService;
    this.jobAdRepository = jobAdRepository;
  }

  @Transactional
  public void saveCandidate(UserDTO user) {
    Optional<Candidate> candidateOpt = candidateRepository.findByUsername(user.getUsername());
    Optional<Candidate> candidateOptByEmail = candidateRepository.findByEmail(user.getEmail());

    if (candidateOpt.isPresent()) {
      throw new UserAlreadyExistsException("Username already exists!");
    }

    if (candidateOptByEmail.isPresent()) {
      throw new UserAlreadyExistsException("Email already exists!");
    }

    candidateRepository.save(new Candidate(
        user.getUsername(),
        passwordEncoder.encode(user.getPassword()),
        user.getName(),
        user.getLastname(),
        user.getEmail()));
  }

  @Transactional
  public void updateCandidate(Candidate updatedCandidate, String password) {
    User user = authenticationService.getCurrentUser();
    Candidate candidate = (Candidate) user;

    if (!passwordEncoder.matches(password, candidate.getPassword())) {
      throw new IncorrectPasswordException("Incorrect password!");
    }

    candidate.setPassword(passwordEncoder.encode(updatedCandidate.getPassword()));
    candidate.setName(updatedCandidate.getName());
    candidate.setLastname(updatedCandidate.getLastname());
    candidateRepository.save(candidate);
  }

  @Transactional
  public boolean deleteCandidate(String deletePassword) {
    User user = authenticationService.getCurrentUser();
    Candidate candidate = (Candidate) user;

    if (!passwordEncoder.matches(deletePassword, candidate.getPassword())) {
      throw new IncorrectPasswordException("Incorrect password!");
    }

    candidateRepository.deleteById(candidate.getUserId());

    return true;
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

  @Transactional(readOnly = true)
  public List<CandidateDTO> getAllCandidatesForJobAd(Long jobAdId) {
    JobAd jobAd = jobAdRepository.findById(jobAdId)
        .orElseThrow(() -> new JobAdNotFoundException("Job Ad does not exist!"));

    List<Candidate> candidates = jobAd.getCandidates();

    return candidates.stream()
        .map(candidate -> new CandidateDTO(
            candidate.getUserId(),
            candidate.getName(),
            candidate.getLastname(),
            candidate.getEmail()
        ))
        .toList();
  }
}
