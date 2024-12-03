package com.dreamwork.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.exception.CvFileSaveException;
import com.dreamwork.exception.JobAdNotFoundException;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.job.Seniority;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
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
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class JobAdServiceTest {

  @Mock
  private JobAdRepository jobAdRepository;

  @Mock
  private CandidateRepository candidateRepository;

  @Mock
  private AuthenticationService authenticationService;

  @InjectMocks
  private JobAdService jobAdService;

  private JobAd jobAd;
  private Candidate candidate;
  private MultipartFile cvFile;

  @BeforeEach
  void setUp() {
    jobAdService = new JobAdService(jobAdRepository, candidateRepository, authenticationService);

    jobAd = new JobAd();
    jobAd.setJobAdId(1L);
    jobAd.setPosition("Java Developer");
    jobAd.setCompany("Apple");
    jobAd.setCountry("Poland");
    jobAd.setCity("Warsaw");
    jobAd.setSeniority(Seniority.JUNIOR);
    jobAd.setMainTechStack("Java, Spring");
    jobAd.setDescription("We are looking for a Java Developer");

    candidate = new Candidate("user", "encodedPassword",
        "John", "Doe", "john.doe@example.com");

    cvFile = mock(MultipartFile.class);
  }

  @Test
  void createJobAd_shouldCreateJobAd_whenAuthenticatedRecruiter() {
    Recruiter recruiter = new Recruiter("user", "encodedPassword",
        "John", "Doe", "john.doe@example.com");

    when(authenticationService.getCurrentUser()).thenReturn(recruiter);

    jobAdService.createJobAd(jobAd);

    assertEquals(jobAd.getRecruiter(), recruiter);
    verify(jobAdRepository, times(1)).save(jobAd);
  }

  @Test
  void deleteJobAd_shouldDeleteJobAd_whenJobAdExists() {
    when(jobAdRepository.findById(1L)).thenReturn(Optional.of(jobAd));

    jobAdService.deleteJobAd(1L);

    verify(jobAdRepository, times(1)).delete(jobAd);
  }

  @Test
  void deleteJobAd_shouldThrowException_whenJobAdDoesNotExist() {
    when(jobAdRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(JobAdNotFoundException.class, () -> jobAdService.deleteJobAd(1L));
  }

  @Test
  void applyToJobAd_shouldApplyToJobAd_whenValidJobAdAndAuthenticatedCandidate() {
    when(jobAdRepository.findById(1L)).thenReturn(Optional.of(jobAd));
    when(authenticationService.getCurrentUser()).thenReturn(candidate);
    when(cvFile.getContentType()).thenReturn("application/pdf");
    when(cvFile.getSize()).thenReturn(1024L);

    jobAdService.applyToJob(jobAd.getJobAdId(), cvFile);

    verify(candidateRepository, times(1)).save(candidate);
    verify(jobAdRepository, times(1)).save(jobAd);
  }

  @Test
  void applyToJobAd_shouldThrowException_whenCvFileIsNotPdf() {
    when(jobAdRepository.findById(1L)).thenReturn(Optional.of(jobAd));
    when(authenticationService.getCurrentUser()).thenReturn(candidate);
    when(cvFile.getContentType()).thenReturn("application/json");

    assertThrows(CvFileSaveException.class, () -> jobAdService.applyToJob(1L, cvFile));
  }

  @Test
  void applyToJobAd_shouldThrowException_whenCvFileSizeExceedsLimit() {
    when(jobAdRepository.findById(1L)).thenReturn(Optional.of(jobAd));
    when(authenticationService.getCurrentUser()).thenReturn(candidate);
    when(cvFile.getContentType()).thenReturn("application/pdf");
    when(cvFile.getSize()).thenReturn(10 * 1024 * 1024 + 1L);

    assertThrows(CvFileSaveException.class, () -> jobAdService.applyToJob(1L, cvFile));
  }

  @Test
  void getJobAdById_shouldReturnJobAd_whenJobAdExists() {
    when(jobAdRepository.findById(1L)).thenReturn(Optional.of(jobAd));

    JobAdDTO foundJobAd = jobAdService.getJobAdById(1L);

    assertEquals(jobAd.getJobAdId(), foundJobAd.getId());
    assertEquals(jobAd.getPosition(), foundJobAd.getPosition());
    assertEquals(jobAd.getCompany(), foundJobAd.getCompany());
    assertEquals(jobAd.getCountry(), foundJobAd.getCountry());
    assertEquals(jobAd.getCity(), foundJobAd.getCity());
    assertEquals(jobAd.getSeniority().toString(), foundJobAd.getSeniority());
    assertEquals(jobAd.getMainTechStack(), foundJobAd.getMainTechStack());
    assertEquals(jobAd.getDescription(), foundJobAd.getDescription());
  }

  @Test
  void getJobAdById_shouldThrowException_whenJobAdDoesNotExist() {
    when(jobAdRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(JobAdNotFoundException.class, () -> jobAdService.getJobAdById(1L));
  }
}
