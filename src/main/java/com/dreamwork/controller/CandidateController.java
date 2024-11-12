package com.dreamwork.controller;

import com.dreamwork.dto.CandidateDTO;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    private CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }


    @PostMapping
    public ResponseEntity<Candidate> createCandidate(@RequestBody Candidate candidate) {
        try {
            Candidate createdCandidate = candidateService.createCandidate(candidate);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCandidate);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{candidateId}")
    public ResponseEntity<Candidate> updateCandidate(@PathVariable Long candidateId, @RequestBody Candidate updatedCandidate) {
        try {
            Candidate candidate = candidateService.updateCandidate(candidateId, updatedCandidate);
            return ResponseEntity.ok(candidate);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/job/{jobAdId}")
    public ResponseEntity<List<CandidateDTO>> getCandidatesByJobId(@PathVariable Long jobAdId) {
        try {
            List<CandidateDTO> candidates = candidateService.getCandidatesByJobId(jobAdId);
            return ResponseEntity.ok(candidates);

        } catch (ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Candidate> deleteCandidate(@PathVariable Long id) {
        try {
            candidateService.deleteCandidate(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
}
