package com.dreamwork.exception;

/**
 * Custom exception thrown when a user not found by username or email.
 * <p>
 * This exception extends RuntimeException and is used to indicate that the current operation cannot
 * be completed because the user is not found in system. It can be thrown when a user is missing or
 * unavailable.
 * <p>
 * The exception accepts a custom message that provides additional details about the error or the
 * specific condition that led to the exception being thrown.
 */
public class UserNotFoundException extends RuntimeException {

  /**
   * Constructs a new UserNotFoundException with the specified detail message.
   *
   * @param message the detail message that explains the reason for the exception.
   */
  public UserNotFoundException(String message) {
    super(message);
  }
}
