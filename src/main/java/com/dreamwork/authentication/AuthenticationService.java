package com.dreamwork.authentication;

import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.repository.CandidateRepository;
import com.dreamwork.repository.RecruiterRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

  private final CandidateRepository candidateRepository;
  private final RecruiterRepository recruiterRepository;

  @Autowired
  public AuthenticationService(CandidateRepository candidateRepository,
      RecruiterRepository recruiterRepository) {
    this.candidateRepository = candidateRepository;
    this.recruiterRepository = recruiterRepository;
  }

  public User getCurrentUser() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    Optional<Candidate> candidateOpt = candidateRepository.findByUsername(username);
    Optional<Recruiter> recruiterOpt = recruiterRepository.findByUsername(username);

    if (candidateOpt.isPresent()) {
      return candidateOpt.get();
    } else if (recruiterOpt.isPresent()) {
      return recruiterOpt.get();
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }
}
