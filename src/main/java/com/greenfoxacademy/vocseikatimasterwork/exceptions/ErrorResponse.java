package com.greenfoxacademy.vocseikatimasterwork.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ErrorResponse {

  private int statusCode;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;
  private String message;
  private String description;

  public static class ErrorResponseBuilder {

    private int statusCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String description;

    public ErrorResponseBuilder() {
    }

    public ErrorResponseBuilder statusCode(int statusCode) {
      this.statusCode = statusCode;
      return this;
    }

    public ErrorResponseBuilder timeStamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public ErrorResponseBuilder message(String message) {
      this.message = message;
      return this;
    }

    public ErrorResponseBuilder description(String description) {
      this.description = description;
      return this;
    }

    public ErrorResponse build() {
      ErrorResponse errorResponse = new ErrorResponse();
      errorResponse.statusCode = this.statusCode;
      errorResponse.timestamp = this.timestamp;
      errorResponse.message = this.message;
      errorResponse.description = this.description;
      return errorResponse;
    }
  }
}
