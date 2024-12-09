package com.dreamwork.exception;

/**
 * Custom exception thrown when a CV (curriculum vitae) file is not found.
 * <p>
 * This exception extends RuntimeException and is used to indicate that the current operation cannot
 * be completed because the Cv file not found on system. It can be thrown when a cv file is missing
 * or unavailable.
 * <p>
 * The exception accepts a custom message that provides more details about the specific situation
 * that led to the file not being found.
 */
public class CvFileNotFoundException extends RuntimeException {

  /**
   * Constructs a new CvFileNotFoundException with the specified detail message.
   *
   * @param message the detail message that explains the reason for the exception.
   */
  public CvFileNotFoundException(String message) {
    super(message);
  }
}
