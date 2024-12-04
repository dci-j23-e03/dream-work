package com.dreamwork.service;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.exception.AlreadyAppliedException;
import com.dreamwork.exception.CvFileSaveException;
import com.dreamwork.exception.JobAdNotFoundException;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.job.Seniority;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.repository.CandidateRepository;
import com.dreamwork.repository.JobAdRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service class for managing job ad operations. This class provides methods for CRUD operations,
 * filtering and managing candidate applications related to job ads.
 */
@Service
public class JobAdService {

  private final JobAdRepository jobAdRepository;
  private final CandidateRepository candidateRepository;
  private final AuthenticationService authenticationService;

  /**
   * constructor for dependencies
   *
   * @param jobAdRepository       Repository for managing JobAd entities
   * @param candidateRepository   Repository for managing Candidate entities
   * @param authenticationService Service for managing authentication
   */
  @Autowired
  public JobAdService(JobAdRepository jobAdRepository, CandidateRepository candidateRepository,
      AuthenticationService authenticationService) {
    this.jobAdRepository = jobAdRepository;
    this.candidateRepository = candidateRepository;
    this.authenticationService = authenticationService;
  }

  /**
   * Retrieves a paginated list of job ads
   *
   * @param page the page number the retrieve
   * @param size the number of items per page
   * @return a page of JobAdDTO with job ads
   */
  @Transactional(readOnly = true)
  public Page<JobAdDTO> getAllJobAds(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    return jobAdRepository.findAll(pageable)
        .map(jobAd -> new JobAdDTO(
            jobAd.getJobAdId(),
            jobAd.getPosition(),
            jobAd.getDate(),
            jobAd.getCompany(),
            jobAd.getCountry(),
            jobAd.getCity(),
            jobAd.getSeniority().toString(),
            jobAd.getMainTechStack(),
            jobAd.getDescription()
        ));
  }

  /**
   * Retrieves all job advertisements.
   *
   * @return A list of JobAdDTO with all job ads.
   */
  @Transactional(readOnly = true)
  public Page<JobAdDTO> getFilteredJobAds(Seniority seniority, String city, String mainTechStack,
      LocalDate date, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    String dateString;
    if (date != null) {
      dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    } else {
      dateString = null;
    }

    Page<JobAd> jobAds = jobAdRepository
        .findAllJobAdsByFiltering(seniority, city, mainTechStack, dateString, pageable);

    return jobAds
        .map(jobAd -> new JobAdDTO(
            jobAd.getJobAdId(),
            jobAd.getPosition(),
            jobAd.getDate(),
            jobAd.getCompany(),
            jobAd.getCountry(),
            jobAd.getCity(),
            jobAd.getSeniority().toString(),
            jobAd.getMainTechStack(),
            jobAd.getDescription()
        ));
  }


  /**
   * Creates a new job ad and associates it with the current recruiter.
   *
   * @param jobAd The JobAd entity to create.
   */
  @Transactional
  public void createJobAd(JobAd jobAd) {
    User user = authenticationService.getCurrentUser();
    Recruiter recruiter = (Recruiter) user;

    jobAd.setRecruiter(recruiter);

    recruiter.getJobAds().add(jobAd);

    jobAdRepository.save(jobAd);
  }

  /**
   * Deletes a job ad by ID, removes associations between job ad and candidates
   *
   * @param jobAdId The ID of the job ad to delete.
   * @throws JobAdNotFoundException if the job ad does not exist.
   */
  @Transactional
  public void deleteJobAd(Long jobAdId) {
    Optional<JobAd> jobAdOpt = jobAdRepository.findById(jobAdId);

    if (jobAdOpt.isEmpty()) {
      throw new JobAdNotFoundException("Job Ad does not exist!");
    }

    JobAd jobAd = jobAdOpt.get();

    for (Candidate candidate : jobAd.getCandidates()) {
      candidate.getAppliedJobAds().remove(jobAd);
      candidateRepository.save(candidate);
    }

    jobAd.getCandidates().clear();

    jobAdRepository.delete(jobAd);
  }

  /**
   * This method allows to apply for a job ad with cv
   *
   * @param jobAdId ID of the JobAd to apply.
   * @param cvFile the CV file of Candidate
   * @throws JobAdNotFoundException if job ad is not found
   * @throws CvFileSaveException if cv file is bigger than 10 mb or in another content type
   */
  @Transactional
  public void applyToJob(Long jobAdId, MultipartFile cvFile) {
    Optional<JobAd> jobAdOpt = jobAdRepository.findById(jobAdId);
    User user = authenticationService.getCurrentUser();
    Candidate candidate = (Candidate) user;

    if (jobAdOpt.isEmpty()) {
      throw new JobAdNotFoundException("Job Ad does not exist!");
    }

    JobAd jobAd = jobAdOpt.get();

    if (jobAd.getCandidates().contains(candidate)) {
      throw new AlreadyAppliedException("You have already applied to this job!");
    }

    if (!"application/pdf".equals(cvFile.getContentType())) {
      throw new CvFileSaveException("Application document must be in .pdf format!");
    }

    if (cvFile.getSize() > 10 * 1024 * 1024) {
      throw new CvFileSaveException("File size must not exceed 10MB!");
    }

    try {
      candidate.setCvFile(null);
      candidate.setCvFileName(null);

      byte[] cvBytes = cvFile.getBytes();
      String cvFileName = cvFile.getOriginalFilename();

      candidate.setCvFile(cvBytes);
      candidate.setCvFileName(cvFileName);
    } catch (IOException e) {
      throw new CvFileSaveException("Error occurred while saving CV file!");
    }

    jobAd.getCandidates().add(candidate);
    candidate.getAppliedJobAds().add(jobAd);

    jobAdRepository.save(jobAd);
    candidateRepository.save(candidate);
  }

  /**
   * Retrieves all job ads the current candidate has applied for.
   *
   * @return A list of JobAdDTO with the candidate's applied job ads.
   */
  @Transactional(readOnly = true)
  public List<JobAdDTO> getAllJobAdsForCandidate() {
    User user = authenticationService.getCurrentUser();
    Candidate candidate = (Candidate) user;

    List<JobAd> appliedJobAds = candidate.getAppliedJobAds();

    return appliedJobAds.stream()
        .map(jobAd -> new JobAdDTO(
            jobAd.getJobAdId(),
            jobAd.getPosition(),
            jobAd.getDate(),
            jobAd.getCompany(),
            jobAd.getCountry(),
            jobAd.getCity(),
            jobAd.getSeniority().toString(),
            jobAd.getMainTechStack(),
            jobAd.getDescription()
        ))
        .toList();
  }

  @Transactional(readOnly = true)
  public boolean hasCandidateAlreadyApplied(Long jobAdId) {
    JobAd jobAd = jobAdRepository.findById(jobAdId)
        .orElseThrow(() -> new JobAdNotFoundException("Job Ad does not exist!"));

    User user = authenticationService.getCurrentUser();
    Candidate candidate = (Candidate) user;

    return jobAd.getCandidates().contains(candidate);
  }

  /**
   * Retrieves all job ads created by the current recruiter.
   *
   * @return A list of JobAdDTO with the recruiter's job ads.
   */
  @Transactional(readOnly = true)
  public List<JobAdDTO> getAllJobAdsForRecruiter() {
    User user = authenticationService.getCurrentUser();
    Recruiter recruiter = (Recruiter) user;

    List<JobAd> jobAds = recruiter.getJobAds();

    return jobAds.stream()
        .map(jobAd -> new JobAdDTO(
            jobAd.getJobAdId(),
            jobAd.getPosition(),
            jobAd.getDate(),
            jobAd.getCompany(),
            jobAd.getCountry(),
            jobAd.getCity(),
            jobAd.getSeniority().toString(),
            jobAd.getMainTechStack(),
            jobAd.getDescription()
        ))
        .toList();
  }

  /**
   * Retrieves a specific job ad by its ID.
   *
   * @param jobAdId The ID of the job ad.
   * @return A JobAdDTO with the job ad details.
   * @throws JobAdNotFoundException if the job ad is not found.
   */
  @Transactional(readOnly = true)
  public JobAdDTO getJobAdById(Long jobAdId) {
    Optional<JobAd> jobAdOpt = jobAdRepository.findById(jobAdId);

    if (jobAdOpt.isEmpty()) {
      throw new JobAdNotFoundException("Job Ad does not exist!");
    }

    JobAd jobAd = jobAdOpt.get();

    return new JobAdDTO(
        jobAd.getJobAdId(),
        jobAd.getPosition(),
        jobAd.getDate(),
        jobAd.getCompany(),
        jobAd.getCountry(),
        jobAd.getCity(),
        jobAd.getSeniority().toString(),
        jobAd.getMainTechStack(),
        jobAd.getDescription()
    );
  }
}
