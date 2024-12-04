package com.dreamwork.authentication;

import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.repository.CandidateRepository;
import com.dreamwork.repository.RecruiterRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Service for handling authentication-related tasks such as retrieving
 * the current authenticated user and logging out the user.
 *
 * This service retrieves user details based on the username
 * of the authenticated user and manages user context.
 */
@Service
public class AuthenticationService {

  private final CandidateRepository candidateRepository;
  private final RecruiterRepository recruiterRepository;

  /**
   * Constructor with the specified repositories for managing candidates and recruiters.
   *
   * @param candidateRepository the repository for accessing candidate data.
   * @param recruiterRepository the repository for accessing recruiter data.
   */
  @Autowired
  public AuthenticationService(CandidateRepository candidateRepository,
      RecruiterRepository recruiterRepository) {
    this.candidateRepository = candidateRepository;
    this.recruiterRepository = recruiterRepository;
  }

  /**
   * Retrieves the current authenticated user (Candidate or Recruiter) based on the username
   * stored in the security context.
   *
   * @return the User object representing the currently authenticated user.
   * @throws UsernameNotFoundException if no user with the current username is found in the repositories.
   */
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

  /**
   * Logs out the current user by clearing the security context and invalidating the HTTP session.
   * This method ensures that the user's authentication details are removed from the context
   * and that their session is properly invalidated to prevent any further access without re-authentication.
   */
  public void logout() {
    SecurityContextHolder.clearContext();

    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();
      HttpServletResponse response = attributes.getResponse();

      if (response != null) {
        new SecurityContextLogoutHandler().logout(request, response, null);
      }
    }
  }
}
