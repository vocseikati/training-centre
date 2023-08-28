package com.greenfoxacademy.vocseikatimasterwork.models;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public abstract class Participant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Positive(message = "ID must be positive (0: invalid).")
  protected Long id;

  @NotEmpty(message = "First name must have a value.")
  protected String firstName;

  @NotEmpty(message = "Last name must have a value.")
  protected String lastName;

  @Enumerated(EnumType.STRING)
  protected Gender gender;

  @Past(message = "Date of birth must be in the past.")
  protected LocalDate dateOfBirth;

  @NotEmpty(message = "Address must have a value.")
  protected String mailingAddress;

  protected String billingAddress;

  @Column(unique = true)
  @Email(message = "Email should be valid.")
  @NotEmpty(message = "Email must have a value.")
  protected String email;

  @NotNull(message = "Phone number must not be null.")
  @Pattern(regexp = "([0-9\\s\\-+]{7,})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$",
      message = "Phone number is invalid.")
  protected String phoneNumber;

}
