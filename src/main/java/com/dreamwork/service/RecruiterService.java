package com.dreamwork.service;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.UserDTO;
import com.dreamwork.exception.IncorrectPasswordException;
import com.dreamwork.exception.UserAlreadyExistsException;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.repository.JobAdRepository;
import com.dreamwork.repository.RecruiterRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing recruiter operations This class handles CRUD operations for
 * candidates, authentication and logic integration
 */
@Service
public class RecruiterService {

  private final RecruiterRepository recruiterRepository;
  private final JobAdRepository jobAdRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationService authenticationService;

  /**
   * Constructor for dependencies.
   *
   * @param recruiterRepository   repository for managing recruiter entities.
   * @param jobAdRepository       repository for managing JobAd entities.
   * @param passwordEncoder       service for encoding password for security.
   * @param authenticationService service for managing authentication.
   */
  @Autowired
  public RecruiterService(RecruiterRepository recruiterRepository, JobAdRepository jobAdRepository,
      PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
    this.recruiterRepository = recruiterRepository;
    this.jobAdRepository = jobAdRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationService = authenticationService;
  }

  /**
   * Saves a new recruiter in database.
   *
   * @param user the user details in a UserDTO object
   * @throws UserAlreadyExistsException if the username or email already exists
   */
  @Transactional
  public void saveRecruiter(UserDTO user) {
    Optional<Recruiter> recruiterOpt = recruiterRepository.findByUsername(user.getUsername());
    Optional<Recruiter> recruiterOptByEmail = recruiterRepository.findByEmail(user.getEmail());

    if (recruiterOpt.isPresent()) {
      throw new UserAlreadyExistsException("Username already exists!");
    }

    if (recruiterOptByEmail.isPresent()) {
      throw new UserAlreadyExistsException("Email already exists!");
    }

    recruiterRepository.save(new Recruiter(
        user.getUsername(),
        passwordEncoder.encode(user.getPassword()),
        user.getName(),
        user.getLastname(),
        user.getEmail()));
  }

  /**
   * Updates the details of the currently logged-in recruiter.
   *
   * @param updatedRecruiter the Recruiter object containing updated details
   * @param password         the current password of the recruiter for verification
   * @throws IncorrectPasswordException if the given password does not match the recruiter's current
   *                                    password
   */
  @Transactional
  public void updateRecruiter(Recruiter updatedRecruiter, String password) {
    User user = authenticationService.getCurrentUser();
    Recruiter recruiter = (Recruiter) user;

    if (!passwordEncoder.matches(password, recruiter.getPassword())) {
      throw new IncorrectPasswordException("Incorrect password!");
    }
    if (updatedRecruiter.getPassword() != null && !updatedRecruiter.getPassword().isEmpty()) {
      recruiter.setPassword(passwordEncoder.encode(updatedRecruiter.getPassword()));
    }
    if (updatedRecruiter.getName() != null && !updatedRecruiter.getName().isEmpty()) {
      recruiter.setName(updatedRecruiter.getName());
    }
    if (updatedRecruiter.getLastname() != null && !updatedRecruiter.getLastname().isEmpty()) {
      recruiter.setLastname(updatedRecruiter.getLastname());
    }

    recruiterRepository.save(recruiter);
  }

  /**
   * Deletes the currently logged-in recruiter from the system. Also removes all job ads associated
   * with the recruiter.
   *
   * @param deletePassword the current password of the recruiter for verification
   * @return true if the deletion is successful
   * @throws IncorrectPasswordException if the given password does not match the recruiter's current
   *                                    password
   */
  @Transactional
  public boolean deleteRecruiter(String deletePassword) {
    User user = authenticationService.getCurrentUser();
    Recruiter recruiter = (Recruiter) user;

    if (!passwordEncoder.matches(deletePassword, recruiter.getPassword())) {
      throw new IncorrectPasswordException("Incorrect password!");
    }

    jobAdRepository.deleteAllJobAdsFromRecruiter(recruiter);
    recruiterRepository.deleteById(recruiter.getUserId());

    return true;
  }
}
