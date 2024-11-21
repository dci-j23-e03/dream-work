package com.dreamwork.repository;

import com.dreamwork.model.user.Recruiter;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {

  Optional<Recruiter> findByUsername(String username);

  Optional<Recruiter> findByEmail(String email);
}
