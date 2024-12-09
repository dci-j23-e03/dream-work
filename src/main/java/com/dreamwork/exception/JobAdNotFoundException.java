package com.dreamwork.exception;

/**
 * Custom exception thrown when a Job Ad is not found.
 * <p>
 * This exception extends RuntimeException and is used to indicate that the current operation cannot
 * be completed because the Job Ad not found on system. It can be thrown when a job ad is missing
 * or unavailable.
 * <p>
 * The exception accepts a custom message that provides more details about the specific situation
 * that led to the job ad not being found.
 */
public class JobAdNotFoundException extends RuntimeException {

  /**
   * Constructs a new JobAdNotFoundException with the specified detail message.
   *
   * @param message the detail message that explains the reason for the exception.
   */
  public JobAdNotFoundException(String message) {
    super(message);
  }
}
