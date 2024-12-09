package com.dreamwork.repository;

import com.dreamwork.model.user.Candidate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Candidate entities.
 * <p>
 * Provides methods for interacting with the database, such as finding candidates by username or
 * email. This interface extends JpaRepository to inherit common CRUD operations.
 */
@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

  /**
   * Finds a candidate by their username.
   *
   * @param username the username of the candidate
   * @return an Optional containing the candidate if found, or empty if not
   */
  Optional<Candidate> findByUsername(String username);

  /**
   * Finds a candidate by their email address.
   *
   * @param email the email address of the candidate
   * @return an  Optional containing the candidate if found, or empty if not
   */
  Optional<Candidate> findByEmail(String email);
}
