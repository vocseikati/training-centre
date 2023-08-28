package com.greenfoxacademy.vocseikatimasterwork.services;

import java.time.LocalDate;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public class ValidationService {

  public static void requireNull(Object o, String message) {
    if (o != null) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void requireNonNull(Object o, String message) {
    if (o == null) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void validateLongId(Long id, String message) {
    requireNonNull(id, message);
    if (id <= 0) {
      throw new IllegalArgumentException(message);
    }
  }

  public static Long isNumeric(String id) {
    try {
      return Long.parseLong(id);
    } catch (NumberFormatException nfe) {
      throw new IllegalArgumentException("Id must be a number.");
    }
  }

  public static void validateDates(LocalDate startDate, LocalDate endDate, String message) {
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void validateBindingResult(BindingResult bindingResult) {
    if (bindingResult != null) {
      List<ObjectError> errors = bindingResult.getAllErrors();
      if (!errors.isEmpty()) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Invalid parameters were found: ");
        for (ObjectError error : errors) {
          messageBuilder.append(error.getDefaultMessage());
        }
        throw new IllegalArgumentException(messageBuilder.toString());
      }
    }
  }
}
