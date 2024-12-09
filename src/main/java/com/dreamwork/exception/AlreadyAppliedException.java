package com.dreamwork.exception;

/**
 * Custom exception thrown when a user trys to apply for a job that they have already applied for.
 * <p>
 * This exception extends RuntimeException and is used to indicate that the current operation cannot
 * be completed because the user has already applied for the job. It can be thrown when a job
 * application process detects that the user has previously applied for the same job.
 * <p>
 * The exception accepts a custom message that provides additional details about the error or the
 * specific condition that led to the exception being thrown.
 */
public class AlreadyAppliedException extends RuntimeException {

  /**
   * Constructs a new AlreadyAppliedException with the specified detail message.
   *
   * @param message the detail message that explains the reason for the exception.
   */
  public AlreadyAppliedException(String message) {
    super(message);
  }
}
