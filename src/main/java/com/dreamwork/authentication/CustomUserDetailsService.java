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

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final CandidateRepository candidateRepository;
  private final RecruiterRepository recruiterRepository;

  @Autowired
  public CustomUserDetailsService(CandidateRepository candidateRepository,
      RecruiterRepository recruiterRepository) {
    this.candidateRepository = candidateRepository;
    this.recruiterRepository = recruiterRepository;
  }

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
