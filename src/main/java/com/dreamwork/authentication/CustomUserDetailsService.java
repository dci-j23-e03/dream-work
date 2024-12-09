package com.dreamwork.authentication;

import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.repository.CandidateRepository;
import com.dreamwork.repository.RecruiterRepository;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This service is responsible for loading user details based on the username, and it checks for
 * both Candidate and Recruiter entities.
 * <p>
 * This service interacts with CandidateRepository and RecruiterRepository to retrieve user data and
 * constructs the necessary UserDetails object. The user roles are dynamically assigned based on the
 * Role of the user.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final CandidateRepository candidateRepository;
  private final RecruiterRepository recruiterRepository;

  /**
   * Constructs a new CustomUserDetailsService instance with the given repositories.
   *
   * @param candidateRepository the CandidateRepository to access candidate data.
   * @param recruiterRepository the RecruiterRepository to access recruiter data.
   */
  @Autowired
  public CustomUserDetailsService(CandidateRepository candidateRepository,
      RecruiterRepository recruiterRepository) {
    this.candidateRepository = candidateRepository;
    this.recruiterRepository = recruiterRepository;
  }

  /**
   * Loads the user details by the provided username.
   *
   * @param username Username of user to load.
   * @return a UserDetails object for the authenticated user.
   * @throws UsernameNotFoundException if no user with provided username found.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Candidate> candidateOpt = candidateRepository.findByUsername(username);
    Optional<Recruiter> recruiterOpt = recruiterRepository.findByUsername(username);

    if (candidateOpt.isPresent()) {
      Candidate candidate = candidateOpt.get();
      Collection<GrantedAuthority> authorities = Collections.singletonList(
          new SimpleGrantedAuthority("ROLE_" + candidate.getRole().name()));
      return new User(candidate.getUsername(), candidate.getPassword(), authorities);
    } else if (recruiterOpt.isPresent()) {
      Recruiter recruiter = recruiterOpt.get();
      Collection<GrantedAuthority> authorities = Collections.singletonList(
          new SimpleGrantedAuthority("ROLE_" + recruiter.getRole().name()));
      return new User(recruiter.getUsername(), recruiter.getPassword(), authorities);
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }
}
