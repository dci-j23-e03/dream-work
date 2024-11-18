package com.dreamwork.service;

import static com.dreamwork.model.job.Seniority.MID_LEVEL;
import static com.dreamwork.model.job.Seniority.SENIOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.dto.UserDTO;
import com.dreamwork.exception.UserNotFoundException;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.job.Seniority;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.repository.CandidateRepository;
import com.dreamwork.repository.JobAdRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class CandidateServiceTest {

  @Mock
  private CandidateRepository candidateRepository;

  @Mock
  private JobAdRepository jobAdRepository;

  @InjectMocks
  private CandidateService candidateService;

  private Candidate candidate1;
  private Candidate candidate2;
  private Recruiter recruiter;
  private JobAd mockJobAd1;
  private JobAd mockJobAd2;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    //candidate1 has applied to two job ads
    candidate1 = new Candidate();
    candidate1.setUserId(1L);
    candidate1.setUsername("johndoe");
    candidate1.setName("John");
    candidate1.setLastname("Doe");
    candidate1.setCountry("USA");
    candidate1.setAppliedJobAds(new ArrayList<>());

    //candidate2 has applied to no job ads
    candidate2 = new Candidate();
    candidate2.setUserId(2L);
    candidate2.setUsername("janedoe");
    candidate2.setName("Jane");
    candidate2.setLastname("Doe");
    candidate2.setCountry("Canada");
    candidate2.setAppliedJobAds(new ArrayList<>());

    recruiter = new Recruiter();
    recruiter.setUserId(2L);
    recruiter.setName("Alice");
    recruiter.setLastname("Smith");
    recruiter.setCompanyName("TechCorp");
    recruiter.setJobAds(new ArrayList<>());

    mockJobAd1 = new JobAd();
    mockJobAd1.setPosition("Software Engineer");
    mockJobAd1.setCountry("USA");
    mockJobAd1.setCity("New York");
    mockJobAd1.setSeniority(MID_LEVEL);
    mockJobAd1.setMainTechStack("Java, Spring Boot");
    mockJobAd1.setDescription("Developing web applications.");
    mockJobAd1.setRecruiter(recruiter);

    mockJobAd2 = new JobAd();
    mockJobAd2.setPosition("Data Scientist");
    mockJobAd2.setCountry("Canada");
    mockJobAd2.setCity("Toronto");
    mockJobAd2.setSeniority(SENIOR);
    mockJobAd2.setMainTechStack("Python, TensorFlow");
    mockJobAd2.setDescription("Building ML models.");
    mockJobAd2.setRecruiter(recruiter);

    candidate1.getAppliedJobAds().add(mockJobAd1);
    candidate1.getAppliedJobAds().add(mockJobAd2);
    recruiter.getJobAds().add(mockJobAd1);
    recruiter.getJobAds().add(mockJobAd2);
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
  void testUpdateCandidate_CorrectPassword(){
    when(candidateRepository.findById(candidate1.getUserId())).thenReturn(Optional.of(candidate1));
    candidate1.setPassword("password");

    Candidate updatedCandidate = new Candidate();
    updatedCandidate.setName("Updated Name");

    candidateService.updateCandidate(candidate1.getUserId(), updatedCandidate, "password");

    verify(candidateRepository).save(candidate1);
  }

  @Test
  void testUpdateCandidate_IncorrectPassword() {
    when(candidateRepository.findById(candidate1.getUserId())).thenReturn(Optional.of(candidate1));
    candidate1.setPassword("password");

    Candidate updatedCandidate = new Candidate();
    updatedCandidate.setName("Updated Name");

    candidateService.updateCandidate(candidate1.getUserId(), updatedCandidate, "password");

    verify(candidateRepository).save(candidate1);
  }

  @Test
  void testUpdateCandidate_ThrowsException_WhenCandidateDoesNotExist(){
    //checking a candidate that does not exist
    when(candidateRepository.findById(3L)).thenReturn(Optional.of(candidate1));

    Candidate updatedCandidate = new Candidate();
    updatedCandidate.setName("Updated Name");

    assertThrows(UserNotFoundException.class, () -> candidateService.updateCandidate(3l, updatedCandidate, "wrong_password"));

  }

  @Test
  void testUpdateCandidate() {
/*
  public Candidate updateCandidate(Long candidateId, Candidate updatedCandidate, String password) {
    Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);
    if (candidateOpt.isEmpty()) {
      throw new UserNotFoundException("Candidate does not exist!");
    }
    Candidate candidate = candidateOpt.get();
    if (!candidate.getPassword().equals(password)) {
      throw new IncorrectPasswordException("Incorrect password!");
    }
    candidate.setPassword(updatedCandidate.getPassword());
    candidate.setName(updatedCandidate.getName());
    candidate.setLastname(updatedCandidate.getLastname());
    candidate.setCountry(updatedCandidate.getCountry());
    candidateRepository.save(candidate);

    return updatedCandidate;
  }
 */
//    //checking a candidate that exists
//    when(candidateRepository.findById(candidate1.getUserId())).thenReturn(Optional.of(candidate1));
//
//    //checking a candidate that does not exist
//    when(candidateRepository.findById(3L)).thenReturn(Optional.empty());
//
//    Candidate updatedCandidate = new Candidate();
//    updatedCandidate.setName("Updated Name");
//
//    assertThrows(UserNotFoundException.class, () -> candidateService.updateCandidate(3l, updatedCandidate, "password"));

    // checking that the candidate enter the correct password
    when(candidateRepository.findById(candidate1.getUserId())).thenReturn(Optional.of(candidate1));
    when(candidate1.getPassword()).thenReturn("password");

    // checking that the candidate does not enter the correct password

    // checking that the method returns the updated candidate

  }

  @Test
  void testDeleteCandidate() {

  }

  @Test
  void testGetAllJobsByCandidateId_ValidCandidateWithJobAds() {
    when(candidateRepository.findById(candidate1.getUserId())).thenReturn(Optional.of(candidate1));
    List<JobAdDTO> actualJobAdDTOs = candidateService.getAllJobAdsByCandidateId(candidate1.getUserId());

    // checking that the method returns the correct number of JobAdDTOs
    assertEquals(2, actualJobAdDTOs.size(), "The method should return 2 JobAdDTOs");

    // checking that the method returns the correct JobAdDTOs
    validateJobAdDTO(mockJobAd1, actualJobAdDTOs.get(0));
    validateJobAdDTO(mockJobAd2, actualJobAdDTOs.get(1));
  }

  // works as helper, not actual test, so no @Test annotation needed
  private void validateJobAdDTO(JobAd expected, JobAdDTO actual) {
    assertEquals(expected.getPosition(), actual.getPosition(), "Position mismatch");
    assertEquals(expected.getCountry(), actual.getCountry(), "Country mismatch");
    assertEquals(expected.getCity(), actual.getCity(), "City mismatch");
    assertEquals(expected.getSeniority().toString(), actual.getSeniority(), "Seniority mismatch");
    assertEquals(expected.getMainTechStack(), actual.getMainTechStack(), "Tech stack mismatch");
    assertEquals(expected.getDescription(), actual.getDescription(), "Description mismatch");
  }

  @Test
  void testGetAllJobsByCandidateId_ValidCandidateWithNoJobAds() {
    when(candidateRepository.findById(candidate2.getUserId())).thenReturn(Optional.of(candidate2));

    List<JobAdDTO> actualJobAdDTOs = candidateService.getAllJobAdsByCandidateId(candidate2.getUserId());

    // Assert: Method returns an empty list
    assertTrue(actualJobAdDTOs.isEmpty(), "The method should return an empty list");
  }

  @Test
  void testGetAllJobsByCandidateId_NonExistentCandidate() {
    // Setup: Mock repository to return Optional.empty() for the candidate
    when(candidateRepository.findById(candidate1.getUserId())).thenReturn(Optional.empty());

    // Assert: Method throws UserNotFoundException
    assertThrows(UserNotFoundException.class, () -> candidateService.getAllJobAdsByCandidateId(candidate1.getUserId()),
        "The method should throw UserNotFoundException when the candidate does not exist");
  }


}
