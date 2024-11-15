package com.dreamwork.repository;

import com.dreamwork.model.user.Candidate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

  Optional<Candidate> findByUsername(String username);
}
