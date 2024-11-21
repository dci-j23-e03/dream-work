package com.dreamwork.controller;

import com.dreamwork.dto.JobAdDTO;
import com.dreamwork.model.user.Candidate;
import com.dreamwork.service.CandidateService;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/candidates")
public class CandidateController {

  private final CandidateService candidateService;

  public CandidateController(@Autowired CandidateService candidateService) {
    this.candidateService = candidateService;
  }

//  @PostMapping("/update")
//  public ResponseEntity<String> updateCandidate(@RequestParam Long candidateId,
//      @RequestBody Candidate updatedCandidate, @RequestParam String password) {
//    candidateService.updateCandidate(updatedCandidate, password);
//    return ResponseEntity.ok("Candidate updated successfully.");
//  }

//  @GetMapping("/candidate-profile")
//  public String getCandidateProfile(Model model, @AuthenticationPrincipal UserPrincipal userPrincipal) {
//    String username = userPrincipal.getUsername();
//    Candidate candidate = candidateService.getCandidateByUsername(username);
//    model.addAttribute("candidate", candidate);
//    return "candidate-profile";
//  }


  @GetMapping("/update")
  public String getUpdateInfo(Model model, Candidate candidate){
    model.addAttribute("candidate", candidate);
    return "candidate-update";
  }

  @PostMapping("/update")
  private String updateRecruiter(@ModelAttribute Candidate candidate, @RequestParam String currentPassword){
    candidateService.updateCandidate(candidate, currentPassword);
    return "candidate-update";
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
