package com.dreamwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for managing the landing page.
 */
@Controller
public class IndexController {

  /**
   * Redirects to the job ads page.
   *
   * @return Redirect to the job ads page.
   */
  @GetMapping
  public String landingPage() {
    return "redirect:/job-ads";
  }
}