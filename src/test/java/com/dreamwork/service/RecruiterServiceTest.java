package com.dreamwork.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.UserDTO;
import com.dreamwork.exception.IncorrectPasswordException;
import com.dreamwork.exception.UserAlreadyExistsException;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.repository.RecruiterRepository;
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
class RecruiterServiceTest {

  @Mock
  private RecruiterRepository recruiterRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private AuthenticationService authenticationService;

  @InjectMocks
  private RecruiterService recruiterService;

  private UserDTO user;
  private Recruiter recruiter;
  private Recruiter updatedRecruiter;

  @BeforeEach
  void setUp() {
    recruiterService = new RecruiterService(
        recruiterRepository, passwordEncoder, authenticationService);

    user = new UserDTO("testUser", "password",
        "John", "Doe", "john.doe@example.com");

    recruiter = new Recruiter("user", "encodedPassword",
        "John", "Doe", "john.doe@example.com");

    updatedRecruiter = new Recruiter("user", "newPassword",
        "Jane", "Doe", "john.doe@example.com");
  }

  @Test
  void saveRecruiter_shouldSaveRecruiter_whenValidUserDTO() {
    when(recruiterRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
    when(recruiterRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

    recruiterService.saveRecruiter(user);

    verify(recruiterRepository, times(1)).save(any(Recruiter.class));
  }

  @Test
  void saveRecruiter_shouldThrowException_whenUsernameExists() {
    when(recruiterRepository.findByUsername(user.getUsername())).thenReturn(
        Optional.of(new Recruiter()));

    assertThrows(UserAlreadyExistsException.class, () -> recruiterService.saveRecruiter(user));
  }

  @Test
  void saveRecruiter_shouldThrowException_whenEmailExists() {
    when(recruiterRepository.findByEmail(user.getEmail())).thenReturn(
        Optional.of(new Recruiter()));

    assertThrows(UserAlreadyExistsException.class, () -> recruiterService.saveRecruiter(user));
  }

  @Test
  void updateRecruiter_shouldUpdateRecruiter_whenPasswordMatches() {
    when(authenticationService.getCurrentUser()).thenReturn(recruiter);
    when(passwordEncoder.matches("password", recruiter.getPassword()))
        .thenReturn(true);

    recruiterService.updateRecruiter(updatedRecruiter, "password");

    assertEquals(recruiter.getPassword(), passwordEncoder.encode(updatedRecruiter.getPassword()));
    assertEquals(recruiter.getName(), updatedRecruiter.getName());
    assertEquals(recruiter.getLastname(), updatedRecruiter.getLastname());

    verify(recruiterRepository, times(1)).save(recruiter);
  }

  @Test
  void updateRecruiter_shouldThrowException_whenPasswordDoesNotMatch() {
    when(authenticationService.getCurrentUser()).thenReturn(recruiter);
    when(passwordEncoder.matches("password", recruiter.getPassword()))
        .thenReturn(false);

    assertThrows(IncorrectPasswordException.class, () ->
        recruiterService.updateRecruiter(updatedRecruiter, "password"));
  }
}
