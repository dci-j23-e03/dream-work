package com.dreamwork.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles errors that happen in the application and provides clear responses to clients.
 * <p>
 * This class uses Spring's ControllerAdvice annotation to catch errors from anywhere in the app.
 * For each type of error, it sends a response with the HTTP status code, status message, and a
 * description of what went wrong.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles UserNotFoundException by returning an error response with HTTP status 404 (Not Found).
   *
   * @param e the exception that was thrown.
   * @return an ErrorResponse with HTTP status 404 (Not Found) containing the reason, and error
   * message.
   */
  @ExceptionHandler(value = UserNotFoundException.class)
  public @ResponseBody ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
    return new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        e.getMessage());
  }

  /**
   * Handles UserAlreadyExistsException by returning an error response with HTTP status 409
   * (Conflict).
   *
   * @param e the exception that was thrown.
   * @return an  ErrorResponse with HTTP status 409 (Conflict) containing the reason, and error
   * message.
   */
  @ExceptionHandler(value = UserAlreadyExistsException.class)
  public @ResponseBody ErrorResponse handleUserAlreadyExistsException(
      UserAlreadyExistsException e) {
    return new ErrorResponse(
        HttpStatus.CONFLICT.value(),
        HttpStatus.CONFLICT.getReasonPhrase(),
        e.getMessage());
  }

  /**
   * Handles JobAdNotFoundException by returning an error response with HTTP status 404 (Not
   * Found).
   *
   * @param e the exception that was thrown.
   * @return an ErrorResponse with HTTP status 404 (Not Found) containing the reason, and error
   * message.
   */
  @ExceptionHandler(value = JobAdNotFoundException.class)
  public @ResponseBody ErrorResponse handleJobAdNotFoundException(JobAdNotFoundException e) {
    return new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        e.getMessage());
  }

  /**
   * Handles IncorrectPasswordException by returning an error response with HTTP status 401
   * (Unauthorized).
   *
   * @param e the exception that was thrown.
   * @return an ErrorResponse with HTTP status 401 (Unauthorized) containing the reason, and error
   * message.
   */
  @ExceptionHandler(value = IncorrectPasswordException.class)
  public @ResponseBody ErrorResponse handleIncorrectPasswordException(
      IncorrectPasswordException e) {
    return new ErrorResponse(
        HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
        e.getMessage());
  }

  /**
   * Handles AlreadyAppliedException by returning an error response with HTTP status 400 (Bad
   * Request).
   *
   * @param e the exception that was thrown.
   * @return an ErrorResponse with HTTP status 400 (Bad Request) containing the reason, and error
   * message.
   */
  @ExceptionHandler(value = AlreadyAppliedException.class)
  public @ResponseBody ErrorResponse handleAlreadyAppliedException(AlreadyAppliedException e) {
    return new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        e.getMessage());
  }

  /**
   * Handles CvFileSaveException by returning an error response with HTTP status 500 (Internal
   * Server Error).
   *
   * @param e the exception that was thrown.
   * @return an ErrorResponse with HTTP status 500 (Internal Server Error) containing the reason,
   * and error message.
   */
  @ExceptionHandler(value = CvFileSaveException.class)
  public @ResponseBody ErrorResponse handleCvFileSaveException(CvFileSaveException e) {
    return new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        e.getMessage());
  }

  /**
   * Handles CvFileNotFoundException by returning an error response with HTTP status 404 (Not
   * Found).
   *
   * @param e the exception that was thrown.
   * @return an ErrorResponse with HTTP status 404 (Not Found) containing the reason, and error
   * message.
   */
  @ExceptionHandler(value = CvFileNotFoundException.class)
  public @ResponseBody ErrorResponse handleCvFileNotFoundException(CvFileNotFoundException e) {
    return new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        e.getMessage());
  }
}
