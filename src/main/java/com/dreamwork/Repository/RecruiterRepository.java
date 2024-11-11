package com.dreamwork.Repository;

import com.dreamwork.model.user.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
Recruiter findByUsername(String username);
}
