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

@Service
public class RecruiterService {

  private final RecruiterRepository recruiterRepository;
  private final JobAdRepository jobAdRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationService authenticationService;

  @Autowired
  public RecruiterService(RecruiterRepository recruiterRepository, JobAdRepository jobAdRepository,
      PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
    this.recruiterRepository = recruiterRepository;
    this.jobAdRepository = jobAdRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationService = authenticationService;
  }

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
