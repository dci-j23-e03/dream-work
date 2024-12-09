package com.dreamwork.repository;

import com.dreamwork.model.user.Recruiter;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Recruiter entities.
 * <p>
 * Provides methods for interacting with the database, such as finding recruiters by username or
 * email. This interface extends JpaRepository to inherit common CRUD operations.
 */
@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {

  /**
   * Finds a recruiter by their username.
   *
   * @param username the username of the recruiter
   * @return an Optional containing the recruiter if found, or empty if not
   */
  Optional<Recruiter> findByUsername(String username);

  /**
   * Finds a recruiter by their email address.
   *
   * @param email the email address of the recruiter
   * @return an  Optional containing the recruiter if found, or empty if not
   */
  Optional<Recruiter> findByEmail(String email);
}
