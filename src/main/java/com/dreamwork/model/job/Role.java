package com.dreamwork.model.job;

/**
 * Enum representing the roles available in the system.
 * <p>
 * Each role defines the type of user and their corresponding permissions or functionality within
 * the application.
 */
public enum Role {

  /**
   * Represents a recruiter who can post job ads and manage candidate applications.
   */
  RECRUITER,

  /**
   * Represents a candidate who can apply for job ads and manage their applications.
   */
  CANDIDATE
}
