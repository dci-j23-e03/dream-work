package com.dreamwork.controller;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.service.CandidateService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

  private final CandidateService candidateService;

  public CandidateController(@Autowired CandidateService candidateService) {
    this.candidateService = candidateService;
  }

  // Needs proper implementation
  @PostMapping("/{candidateId}")
  public ResponseEntity<Candidate> updateCandidate(@PathVariable Long candidateId,
      @RequestBody Candidate updatedCandidate) {
    try {
      Candidate candidate = candidateService.updateCandidate(candidateId, updatedCandidate);
      return ResponseEntity.ok(candidate);
    } catch (ResponseStatusException e) {
      return ResponseEntity.status(e.getStatusCode()).build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/delete")
  public ResponseEntity<Candidate> deleteCandidate(@RequestParam Long candidateId) {
    candidateService.deleteCandidate(candidateId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/job-ads")
  public ResponseEntity<List<JobAdDTO>> getAllJobAdsByCandidateId(@RequestParam Long candidateId) {
    List<JobAdDTO> jobAdDTOs = candidateService.getAllJobAdsByCandidateId(candidateId);
    return ResponseEntity.ok(jobAdDTOs);
  }
}
