package com.dreamwork.controller;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.service.CandidateService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/candidates")
public class CandidateController {

  private final CandidateService candidateService;

  public CandidateController(@Autowired CandidateService candidateService) {
    this.candidateService = candidateService;
  }

  @PostMapping("/update")
  public ResponseEntity<String> updateCandidate(@RequestParam Long candidateId,
      @RequestBody Candidate updatedCandidate, @RequestParam String password) {
    candidateService.updateCandidate(candidateId, updatedCandidate, password);
    return ResponseEntity.ok("Candidate updated successfully.");
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

  @GetMapping("/{candidateId}/view-cv")
  public ResponseEntity<byte[]> viewCv(@PathVariable Long candidateId) {
    Candidate candidate = candidateService.viewCv(candidateId);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition
        .inline()
        .filename(candidate.getCvFileName())
        .build());

    return ResponseEntity.status(HttpStatus.OK).headers(headers).body(candidate.getCvFile());
  }
}
