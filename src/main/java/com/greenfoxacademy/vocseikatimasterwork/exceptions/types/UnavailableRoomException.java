package com.greenfoxacademy.vocseikatimasterwork.exceptions.types;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnavailableRoomException extends ResponseStatusException {

  public UnavailableRoomException(String reason) {
    super(HttpStatus.NOT_FOUND, reason);
  }
}
