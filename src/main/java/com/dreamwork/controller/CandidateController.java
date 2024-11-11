package com.dreamwork.controller;

import com.dreamwork.model.user.Candidate;
import com.dreamwork.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    private CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public List<Candidate> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
        return candidateService.getCandidateById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Candidate createCandidate(@RequestBody Candidate candidate) {
        return candidateService.saveCandidate(candidate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateCandidate(@PathVariable Long id, @RequestBody Candidate updatedCandidate) {
        return candidateService.getCandidateById(id)
                .map(existingCandidate -> {
                    existingCandidate.setUsername(updatedCandidate.getUsername());
                    existingCandidate.setPassword(updatedCandidate.getPassword());
                    existingCandidate.setName(updatedCandidate.getName());
                    existingCandidate.setLastname(updatedCandidate.getLastname());
                    existingCandidate.setCountry(updatedCandidate.getCountry());
                    return ResponseEntity.ok(candidateService.saveCandidate(existingCandidate));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Candidate> deleteCandidate(@PathVariable Long id) {
        if (candidateService.getCandidateById(id).isPresent()) {
            candidateService.deleteCandidate(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping("/{candidateId}/apply/{jobAdId}")
//    public ResponseEntity<Candidate> applyToJob(@PathVariable Long candidateId, @PathVariable Long jobAdId) {
//        try {
//            Candidate candidate = candidateService.applyToJob(candidateId, jobAdId);
//            return ResponseEntity.ok(candidate);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }


}
