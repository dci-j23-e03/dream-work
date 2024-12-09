package com.dreamwork.exception;

/**
 * Custom exception thrown when the password not correct .
 * <p>
 * This exception extends RuntimeException and is used to indicate that the current operation cannot
 * be completed because the password is not correct.
 * <p>
 * The exception accepts a custom message that provides more details about the specific situation
 * that led to the incorrect password.
 */
public class IncorrectPasswordException extends RuntimeException {

  /**
   * Constructs a new IncorrectPasswordException with the specified detail message.
   *
   * @param message the detail message that explains the reason for the exception.
   */
  public IncorrectPasswordException(String message) {
    super(message);
  }
}
