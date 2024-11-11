package com.dreamwork.service;

import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.respository.CandidateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    /* will need to make sure that the JobAdRepo is aligned with Nenad */
//    private final JobAdRepository jobAdRepository;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }


//    @Autowired
//    public CandidateService(CandidateRepository candidateRepository, JobAdRepository jobAdRepository) {
//        this.candidateRepository = candidateRepository;
//        this.jobAdRepository = jobAdRepository;
//    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Optional<Candidate> getCandidateById(Long id) {
        return candidateRepository.findById(id);
    }


    public Candidate saveCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }

//    @Transactional
//    public Candidate applyToJob(Long candidateId, Long jobAdId) {
//        Candidate candidate = candidateRepository.findById(candidateId)
//                .orElseThrow(() -> new IllegalStateException("Candidate with id " + candidateId + " not found"));
//        JobAd jobAd = jobAdRepository.findById(jobAdId)
//                .orElseThrow(() -> new IllegalStateException("Job Ad with id " + jobAdId + " not found"));
//
//        candidate.getAppliedJobAds().add(jobAd);
//        return CandidateRepository.save(candidate);
//    }
}
