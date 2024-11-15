package com.dreamwork.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dreamwork.dto.UserDTO;
import com.dreamwork.model.job.Seniority;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.repository.CandidateRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class CandidateServiceTest {

  @Mock
  private CandidateRepository candidateRepository;

  @InjectMocks
  private CandidateService candidateService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSaveCandidate() {
    UserDTO user = new UserDTO("johndoe", "password", "John",
        "Doe", Seniority.JUNIOR.toString());
    when(candidateRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
    candidateService.saveCandidate(user);

    verify(candidateRepository, times(1)).save(any(Candidate.class));

  }

  @Test
  void testUpdateCandidate() {

  }

  @Test
  void testDeleteCandidate() {

  }

  @Test
  void testGetAllJobAdsByCandidateId() {

  }


}
