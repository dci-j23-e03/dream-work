package com.dreamwork.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

  private int statusCode;
  private String status;
  private String message;
}
