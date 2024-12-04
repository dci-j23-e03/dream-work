package com.dreamwork.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = UserNotFoundException.class)
  public @ResponseBody ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
    return new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        e.getMessage());
  }

  @ExceptionHandler(value = UserAlreadyExistsException.class)
  public @ResponseBody ErrorResponse handleUserAlreadyExistsException(
      UserAlreadyExistsException e) {
    return new ErrorResponse(
        HttpStatus.CONFLICT.value(),
        HttpStatus.CONFLICT.getReasonPhrase(),
        e.getMessage());
  }

  @ExceptionHandler(value = JobAdNotFoundException.class)
  public @ResponseBody ErrorResponse handleJobAdNotFoundException(JobAdNotFoundException e) {
    return new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        e.getMessage());
  }

  @ExceptionHandler(value = IncorrectPasswordException.class)
  public @ResponseBody ErrorResponse handleIncorrectPasswordException(
      IncorrectPasswordException e) {
    return new ErrorResponse(
        HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
        e.getMessage());
  }

  @ExceptionHandler(value = AlreadyAppliedException.class)
  public @ResponseBody ErrorResponse handleAlreadyAppliedException(AlreadyAppliedException e) {
    return new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        e.getMessage());
  }

  @ExceptionHandler(value = CvFileSaveException.class)
  public @ResponseBody ErrorResponse handleCvFileSaveException(CvFileSaveException e) {
    return new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        e.getMessage());
  }

  @ExceptionHandler(value = CvFileNotFoundException.class)
  public @ResponseBody ErrorResponse handleCvFileNotFoundException(CvFileNotFoundException e) {
    return new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        e.getMessage());
  }
}
