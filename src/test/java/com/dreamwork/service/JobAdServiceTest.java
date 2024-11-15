package com.dreamwork.service;

import static com.dreamwork.model.job.Seniority.MID_LEVEL;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.repository.CandidateRepository;
import com.dreamwork.repository.JobAdRepository;
import com.dreamwork.repository.RecruiterRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

public class JobAdServiceTest {

  @Mock
  private JobAdRepository jobAdRepository;

  @Mock
  private RecruiterRepository recruiterRepository;

  @Mock
  private CandidateRepository candidateRepository;

  @InjectMocks
  private JobAdService jobAdService;

  private Recruiter recruiter;
  private JobAd jobAd;
  private Candidate candidate;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    recruiter = new Recruiter();
    recruiter.setUserId(1L);
    recruiter.setJobAds(new ArrayList<>());

    jobAd = new JobAd();
    jobAd.setJobAdId(1L);
    jobAd.setCandidates(new ArrayList<>());

    candidate = new Candidate();
    candidate.setUserId(2L);
    candidate.setAppliedJobAds(new ArrayList<>());
  }

  @Test
  void testGetAllJobAds() {
    JobAd jobAd = new JobAd();
    jobAd.setPosition("Software Engineer");
    jobAd.setCountry("USA");
    jobAd.setCity("New York");
    jobAd.setSeniority(MID_LEVEL);
    jobAd.setMainTechStack("Java, Spring Boot");
    jobAd.setDescription("Developing web applications.");

    Recruiter recruiter1 = new Recruiter();
    recruiter1.setName("John");
    recruiter1.setLastname("Williams");
    recruiter1.setCompanyName("Google");
    jobAd.setRecruiter(recruiter1);

    List<JobAd> jobAds = List.of(jobAd);

    when(jobAdRepository.findAll()).thenReturn(jobAds);

    List<JobAdDTO> jobAdDTOS = jobAdService.getAllJobAds();

    verify(jobAdRepository).findAll();
    assertEquals(1, jobAdDTOS.size());
    JobAdDTO jobAdDTO = jobAdDTOS.get(0);
    assertEquals(jobAd.getPosition(), jobAdDTO.getPosition());
    assertEquals(jobAd.getCountry(), jobAdDTO.getCountry());
    assertEquals(jobAd.getCity(), jobAdDTO.getCity());
    assertEquals(jobAd.getSeniority().toString(), jobAdDTO.getSeniority().toString());
    assertEquals(jobAd.getMainTechStack(), jobAdDTO.getMainTechStack());
    assertEquals(jobAd.getDescription(), jobAdDTO.getDescription());
    assertEquals(recruiter1.getName(), jobAdDTO.getRecruiter().getName());
    assertEquals(recruiter1.getLastname(), jobAdDTO.getRecruiter().getLastname());
    assertEquals(recruiter1.getCompanyName(), jobAdDTO.getRecruiter().getCompanyName());
  }

  @Test
  void testCreateJobAd() {
    when(recruiterRepository.findById(recruiter.getUserId())).thenReturn(Optional.of(recruiter));
    when(jobAdRepository.save(any(JobAd.class))).thenReturn(jobAd);

    jobAdService.createJobAd(jobAd, recruiter.getUserId());
    assertEquals(recruiter, jobAd.getRecruiter());
    verify(jobAdRepository).save(jobAd);
  }


  @SneakyThrows
  @Test
  void testApplyToJob() {
    MultipartFile cvFile = mock(MultipartFile.class);
    byte[] mockCvBytes = new byte[]{0x1, 0x2, 0x3};
    when(cvFile.getBytes()).thenReturn(mockCvBytes);
    when(cvFile.getOriginalFilename()).thenReturn("resume.pdf");

    when(jobAdRepository.findById(jobAd.getJobAdId())).thenReturn(Optional.of(jobAd));
    when(candidateRepository.findById(candidate.getUserId())).thenReturn(Optional.of(candidate));

    jobAdService.applyToJob(jobAd.getJobAdId(), candidate.getUserId(), cvFile);

    verify(jobAdRepository).findById(jobAd.getJobAdId());
    verify(candidateRepository).findById(candidate.getUserId());

    assertTrue(jobAd.getCandidates().contains(candidate),
        " Candidate should be added to the job ad");
    assertTrue(candidate.getAppliedJobAds().contains(jobAd),
        "Job ad should be added to the candidate");


    assertArrayEquals(mockCvBytes, candidate.getCvFile(),
        "CV file should be saved to the candidate");
    assertEquals("resume.pdf", candidate.getCvFileName(),
        "CV file name should be saved to the candidate");
  }
}
