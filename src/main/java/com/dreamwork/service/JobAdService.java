package com.dreamwork.service;

import com.dreamwork.authentication.AuthenticationService;
import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.exception.CvFileSaveException;
import com.dreamwork.exception.JobAdNotFoundException;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.model.user.User;
import com.dreamwork.repository.CandidateRepository;
import com.dreamwork.repository.JobAdRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class JobAdService {

  private final JobAdRepository jobAdRepository;
  private final CandidateRepository candidateRepository;
  private final AuthenticationService authenticationService;

  @Autowired
  public JobAdService(JobAdRepository jobAdRepository, CandidateRepository candidateRepository,
      AuthenticationService authenticationService) {
    this.jobAdRepository = jobAdRepository;
    this.candidateRepository = candidateRepository;
    this.authenticationService = authenticationService;
  }

  @Transactional(readOnly = true)
  public List<JobAdDTO> getAllJobAds() {
    List<JobAd> jobAds = jobAdRepository.findAll();

    return jobAds.stream()
        .map(jobAd -> new JobAdDTO(
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


  @Transactional
  public void createJobAd(JobAd jobAd) {
    User user = authenticationService.getCurrentUser();
    Recruiter recruiter = (Recruiter) user;

    jobAd.setRecruiter(recruiter);

    recruiter.getJobAds().add(jobAd);

    jobAdRepository.save(jobAd);
  }

  @Transactional
  public void applyToJob(Long jobAdId, MultipartFile cvFile) {
    Optional<JobAd> jobAdOpt = jobAdRepository.findById(jobAdId);
    User user = authenticationService.getCurrentUser();
    Candidate candidate = (Candidate) user;

    if (jobAdOpt.isEmpty()) {
      throw new JobAdNotFoundException("Job Ad does not exist!");
    }

    if (!"application/pdf".equals(cvFile.getContentType())) {
      throw new CvFileSaveException("CV file must be in PDF format!");
    }

    JobAd jobAd = jobAdOpt.get();

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

  @Transactional(readOnly = true)
  public List<JobAdDTO> getAllJobAdsForCandidate() {
    User user = authenticationService.getCurrentUser();
    Candidate candidate = (Candidate) user;

    List<JobAd> appliedJobAds = candidate.getAppliedJobAds();

    return appliedJobAds.stream()
        .map(jobAd -> new JobAdDTO(
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
  public List<JobAdDTO> getAllJobAdsForRecruiter() {
    User user = authenticationService.getCurrentUser();
    Recruiter recruiter = (Recruiter) user;

    List<JobAd> jobAds = recruiter.getJobAds();

    return jobAds.stream()
        .map(jobAd -> new JobAdDTO(
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
}
