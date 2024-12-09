package com.dreamwork.exception;

/**
 * Custom exception thrown when a user by registration already exists by username or email.
 * <p>
 * This exception extends RuntimeException and is used to indicate that the current operation cannot
 * be completed because the user is already exists in system. It can be thrown when someone wants to
 * register with existing username or email.
 * <p>
 * The exception accepts a custom message that provides additional details about the error or the
 * specific condition that led to the exception being thrown.
 */
public class UserAlreadyExistsException extends RuntimeException {

  /**
   * Constructs a new UserAlreadyExistsException with the specified detail message.
   *
   * @param message the detail message that explains the reason for the exception.
   */
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
