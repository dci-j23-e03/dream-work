package com.dreamwork.service;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.dto.RecruiterDTO;
import com.dreamwork.exception.CvFileSaveException;
import com.dreamwork.exception.JobAdNotFoundException;
import com.dreamwork.exception.UserNotFoundException;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.repository.CandidateRepository;
import com.dreamwork.repository.JobAdRepository;
import com.dreamwork.repository.RecruiterRepository;
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
  private final RecruiterRepository recruiterRepository;
  private final CandidateRepository candidateRepository;

  @Autowired
  public JobAdService(JobAdRepository jobAdRepository, RecruiterRepository recruiterRepository,
      CandidateRepository candidateRepository) {
    this.jobAdRepository = jobAdRepository;
    this.recruiterRepository = recruiterRepository;
    this.candidateRepository = candidateRepository;
  }

  @Transactional(readOnly = true)
  public List<JobAdDTO> getAllJobAds() {
    List<JobAd> jobAds = jobAdRepository.findAll();

    return jobAds.stream()
        .map(jobAd -> new JobAdDTO(
            jobAd.getPosition(),
            jobAd.getCountry(),
            jobAd.getCity(),
            jobAd.getSeniority().toString(),
            jobAd.getMainTechStack(),
            jobAd.getDescription(),
            new RecruiterDTO(
                jobAd.getRecruiter().getName(),
                jobAd.getRecruiter().getLastname(),
                jobAd.getRecruiter().getCompanyName())
        ))
        .toList();
  }


  @Transactional
  public void createJobAd(JobAd jobAd, Long recruiterId) {
    Optional<Recruiter> recruiterOpt = recruiterRepository.findById(recruiterId);
    if (recruiterOpt.isEmpty()) {
      throw new UserNotFoundException("Recruiter does not exist!");
    }

    Recruiter recruiter = recruiterOpt.get();

    jobAd.setRecruiter(recruiter);

    recruiter.getJobAds().add(jobAd);

    jobAdRepository.save(jobAd);
  }

  @Transactional
  public void applyToJob(Long jobAdId, Long candidateId, MultipartFile cvFile) {
    Optional<JobAd> jobAdOpt = jobAdRepository.findById(jobAdId);
    Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);

    if (jobAdOpt.isEmpty()) {
      throw new JobAdNotFoundException("Job Ad does not exist!");
    }

    if (candidateOpt.isEmpty()) {
      throw new UserNotFoundException("Candidate does not exist!");
    }

    if (!"application/pdf".equals(cvFile.getContentType())) {
      throw new CvFileSaveException("CV file must be in PDF format!");
    }

    JobAd jobAd = jobAdOpt.get();
    Candidate candidate = candidateOpt.get();

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
}
