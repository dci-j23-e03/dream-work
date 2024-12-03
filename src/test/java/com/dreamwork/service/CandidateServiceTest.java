package com.dreamwork.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.UserDTO;
import com.dreamwork.exception.CvFileNotFoundException;
import com.dreamwork.exception.IncorrectPasswordException;
import com.dreamwork.exception.UserAlreadyExistsException;
import com.dreamwork.exception.UserNotFoundException;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.repository.CandidateRepository;
import com.dreamwork.repository.JobAdRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

  @Mock
  private CandidateRepository candidateRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private AuthenticationService authenticationService;

  @Mock
  private JobAdRepository jobAdRepository;

  @InjectMocks
  private CandidateService candidateService;

  private UserDTO user;
  private Candidate candidate;
  private Candidate updatedCandidate;

  @BeforeEach
  void setUp() {
    candidateService = new CandidateService(candidateRepository, passwordEncoder,
        authenticationService, jobAdRepository);

    user = new UserDTO("testUser", "password",
        "John", "Doe", "john.doe@example.com");

    candidate = new Candidate("user", "encodedPassword",
        "John", "Doe", "john.doe@example.com");

    updatedCandidate = new Candidate("user", "newPassword",
        "Jane", "Doe", "john.doe@example.com");
  }

  @Test
  void saveCandidate_shouldSaveCandidate_whenValidUserDTO() {
    when(candidateRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
    when(candidateRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

    candidateService.saveCandidate(user);

    verify(candidateRepository, times(1)).save(any(Candidate.class));
  }

  @Test
  void saveCandidate_shouldThrowException_whenUsernameExists() {
    when(candidateRepository.findByUsername(user.getUsername())).thenReturn(
        Optional.of(new Candidate()));

    assertThrows(UserAlreadyExistsException.class, () -> candidateService.saveCandidate(user));
  }

  @Test
  void saveCandidate_shouldThrowException_whenEmailExists() {
    when(candidateRepository.findByEmail(user.getEmail())).thenReturn(
        Optional.of(new Candidate()));

    assertThrows(UserAlreadyExistsException.class, () -> candidateService.saveCandidate(user));
  }

  @Test
  void updateCandidate_shouldUpdateCandidate_whenPasswordMatches() {
    when(authenticationService.getCurrentUser()).thenReturn(candidate);
    when(passwordEncoder.matches("password", candidate.getPassword()))
        .thenReturn(true);

    candidateService.updateCandidate(updatedCandidate, "password");

    assertEquals(candidate.getPassword(), passwordEncoder.encode(updatedCandidate.getPassword()));
    assertEquals(candidate.getName(), updatedCandidate.getName());
    assertEquals(candidate.getLastname(), updatedCandidate.getLastname());

    verify(candidateRepository, times(1)).save(candidate);
  }

  @Test
  void updateCandidate_shouldThrowException_whenPasswordDoesNotMatch() {
    when(authenticationService.getCurrentUser()).thenReturn(candidate);
    when(passwordEncoder.matches("password", candidate.getPassword()))
        .thenReturn(false);

    assertThrows(IncorrectPasswordException.class, () ->
        candidateService.updateCandidate(updatedCandidate, "password"));
  }

  @Test
  void deleteCandidate_shouldDeleteCandidate_whenPasswordMatches() {
    when(authenticationService.getCurrentUser()).thenReturn(candidate);
    when(passwordEncoder.matches("password", candidate.getPassword()))
        .thenReturn(true);

    boolean result = candidateService.deleteCandidate("password");

    assertTrue(result);
    verify(candidateRepository, times(1)).deleteById(candidate.getUserId());
  }

  @Test
  void deleteCandidate_shouldThrowException_whenPasswordDoesNotMatch() {
    when(authenticationService.getCurrentUser()).thenReturn(candidate);
    when(passwordEncoder.matches("password", candidate.getPassword()))
        .thenReturn(false);

    assertThrows(IncorrectPasswordException.class, () ->
        candidateService.deleteCandidate("password"));
  }

  @Test
  void viewCv_shouldThrowException_whenCandidateNotFound() {
    when(candidateRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> candidateService.viewCv(1L));
  }

  @Test
  void viewCv_shouldThrowException_whenCvFileNotFound() {
    candidate.setUserId(1L);

    when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));

    assertThrows(CvFileNotFoundException.class, () -> candidateService.viewCv(1L));
  }
}
