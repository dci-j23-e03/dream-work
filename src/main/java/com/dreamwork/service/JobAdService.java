package com.dreamwork.service;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.job.Seniority;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.model.user.Recruiter;
import com.dreamwork.repository.CandidateRepository;
import com.dreamwork.repository.JobAdRepository;
import com.dreamwork.repository.RecruiterRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public List<JobAd> getAllJobAds() {
    return jobAdRepository.findAll();
  }


  @Transactional
  public JobAd createJobAd(JobAdDTO jobAdDto, Long recruiterId) {
    Optional<Recruiter> recruiterOpt = recruiterRepository.findById(recruiterId);
    if (!recruiterOpt.isPresent()) {
      throw new IllegalArgumentException("Recruiter with ID " + recruiterId + " not found.");
    }

    Recruiter recruiter = recruiterOpt.get();
    JobAd jobAd = new JobAd(
        jobAdDto.getPosition(),
        jobAdDto.getCountry(),
        jobAdDto.getCity(),
        Seniority.valueOf(jobAdDto.getSeniority()),
        jobAdDto.getMainTechStack(),
        jobAdDto.getDescription(),
        recruiter);

    recruiter.getJobAds().add(jobAd);

    return jobAdRepository.save(jobAd);
  }

  @Transactional
  public void applyToJob(Long jobAdId, Long candidateId) {
    Optional<JobAd> jobAdOpt = jobAdRepository.findById(jobAdId);
    Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);

    if (!jobAdOpt.isPresent()) {
      throw new IllegalArgumentException("Job ad with ID " + jobAdId + " not found.");
    }

    if (!candidateOpt.isPresent()) {
      throw new IllegalArgumentException("Candidate with ID " + candidateId + " not found.");
    }

    JobAd jobAd = jobAdOpt.get();
    Candidate candidate = candidateOpt.get();

    jobAd.getCandidates().add(candidate);
    candidate.getAppliedJobAds().add(jobAd);

    jobAdRepository.save(jobAd);
    candidateRepository.save(candidate);
  }
}
