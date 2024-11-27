package com.dreamwork.service;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.UserDTO;
import com.dreamwork.exception.IncorrectPasswordException;
import com.dreamwork.exception.UserAlreadyExistsException;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.repository.RecruiterRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecruiterService {

  private final RecruiterRepository recruiterRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationService authenticationService;

  @Autowired
  public RecruiterService(RecruiterRepository recruiterRepository,
      PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
    this.recruiterRepository = recruiterRepository;
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

    recruiter.setPassword(passwordEncoder.encode(updatedRecruiter.getPassword()));
    recruiter.setName(updatedRecruiter.getName());
    recruiter.setLastname(updatedRecruiter.getLastname());
    recruiterRepository.save(recruiter);
  }

  @Transactional
  public boolean deleteRecruiter(String deletePassword) {
    User user = authenticationService.getCurrentUser();
    Recruiter recruiter = (Recruiter) user;
    if (!passwordEncoder.matches(deletePassword, recruiter.getPassword())) {
      throw new IncorrectPasswordException("Incorrect password!");
    }
    recruiterRepository.deleteById(recruiter.getUserId());
    return true;
  }
}
