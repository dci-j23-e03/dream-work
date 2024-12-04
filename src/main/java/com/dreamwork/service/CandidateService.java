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

/**
 * Service class for managing candidate operations
 * This class handles CRUD operations for candidates, authentication and logic integration
 */
@Service
public class CandidateService {

  private final CandidateRepository candidateRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationService authenticationService;
  private final JobAdRepository jobAdRepository;

  /**
   * Constructor for dependencies
   *
   * @param candidateRepository Repository for managing Candidate entities
   * @param passwordEncoder Encoder for security
   * @param authenticationService Service for managing authenticated user operations
   * @param jobAdRepository Repository for managing Job Ad entities
   */
  @Autowired
  public CandidateService(CandidateRepository candidateRepository, PasswordEncoder passwordEncoder,
      AuthenticationService authenticationService, JobAdRepository jobAdRepository) {
    this.candidateRepository = candidateRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationService = authenticationService;
    this.jobAdRepository = jobAdRepository;
  }

  /**
   * This method saves a new candidate to the database
   *
   * @param user the user details (username, password, name, lastname, email)
   * @throws UserAlreadyExistsException if username or email already exists.
   */
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

  /**
   * This method updates authenticated Candidate details
   *
   * @param updatedCandidate The updated Candidate details
   * @param password The current password for authentication
   * @throws IncorrectPasswordException if the password is invalid
   */
  @Transactional
  public void updateCandidate(Candidate updatedCandidate, String password) {
    User user = authenticationService.getCurrentUser();
    Candidate candidate = (Candidate) user;

    if (!passwordEncoder.matches(password, candidate.getPassword())) {
      throw new IncorrectPasswordException("Incorrect password!");
    }
    if (updatedCandidate.getPassword() != null && !updatedCandidate.getPassword().isEmpty()) {
      candidate.setPassword(passwordEncoder.encode(updatedCandidate.getPassword()));
    }
    if (updatedCandidate.getName() != null && !updatedCandidate.getName().isEmpty()) {
      candidate.setName(updatedCandidate.getName());
    }
    if (updatedCandidate.getLastname() != null && !updatedCandidate.getLastname().isEmpty()) {
      candidate.setLastname(updatedCandidate.getLastname());
    }

    candidateRepository.save(candidate);
  }

  /**
   * deletes the candidate's account
   *
   * @param deletePassword the password for authentication.
   * @return true if the deletion was successful.
   * @throws IncorrectPasswordException if the password is invalid
   */
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

  /**
   * this method retrieves the cv file of a candidate by their ID
   *
   * @param candidateId the ID of the candidate
   * @return Candidate entity with CV details
   * @throws UserNotFoundException if the candidate does not exist.
   * @throws CvFileNotFoundException if the cv file is not found.
   */
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

  /**
   * this method retrieves all candidates who applid for a specific job ad
   *
   *
   * @param jobAdId the ID of the job ad
   * @return list of CandidateDTO objects representing the candidates
   * @throws JobAdNotFoundException if the job ad is not found
   */
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
