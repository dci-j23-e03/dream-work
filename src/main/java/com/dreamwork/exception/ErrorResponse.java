package com.dreamwork.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A class representing the structure of an error response returned.
 * <p>
 * This class is typically used in API responses to provide error information when an exception
 * occurs or when a request cannot be processed successfully. It includes details such as the HTTP
 * status code, the status of the response (e.g., "error"), and a message providing more context
 * about the error.
 *
 * <p>The class uses Lombok annotations @Data and @AllArgsConstructor to automatically generate
 * the getter, setter, `toString`, `equals`, and `hashCode` methods, along with a constructor
 * accepting all fields.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

  /**
   * The HTTP status code of the error (e.g., 404- "Not Found").
   */
  private int statusCode;

  /**
   * The status of the response, typically "error" for error cases.
   */
  private String status;

  /**
   * A detail message that explains the reason for the exception.
   */
  private String message;
}
