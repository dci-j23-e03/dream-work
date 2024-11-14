package com.dreamwork.service;

import com.dreamwork.dto.CandidateDTO;
import com.dreamwork.model.job.JobAd;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.repository.CandidateRepository;
import com.dreamwork.repository.JobAdRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final JobAdRepository jobAdRepository;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository, JobAdRepository jobAdRepository) {
        this.candidateRepository = candidateRepository;
        this.jobAdRepository = jobAdRepository;
    }

    @Transactional
    public Candidate createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Transactional
    public Candidate updateCandidate(Long candidateId, Candidate updatedCandidate) {
        Candidate existingCandidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate with id " + candidateId + " not found"));

        existingCandidate.setName(updatedCandidate.getName());
        existingCandidate.setLastname(updatedCandidate.getLastname());
        existingCandidate.setCountry(updatedCandidate.getCountry());

        return candidateRepository.save(existingCandidate);
    }

    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate with id " + id + " not found"));
    }

    public List<CandidateDTO> getCandidatesByJobId(Long jobAdId) {
        JobAd jobAd = jobAdRepository.findById(jobAdId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job Ad with id " + jobAdId + " not found"));

        List<CandidateDTO> candidatePublicView = jobAd.getCandidates().stream()
                .map(candidate -> new CandidateDTO(
                        candidate.getName(),
                        candidate.getLastname(),
                        candidate.getCountry()))
                .collect(Collectors.toList());

        if (jobAd.getCandidates().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No candidates applied to this job ad");
        }
        return candidatePublicView;

    }

    @Transactional
    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate with id " + id + " not found");
        }
        candidateRepository.deleteById(id);
        if (candidateRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidate with id " + id + " not deleted");
        }
    }

}
