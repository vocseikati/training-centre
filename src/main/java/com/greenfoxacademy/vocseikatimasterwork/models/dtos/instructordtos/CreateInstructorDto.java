package com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Instructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(discriminator = "Data of the instructor you want to create.")
public class CreateInstructorDto {

  @ApiModelProperty(value = "ID of the instructor.")
  private Long id;

  @ApiModelProperty(value = "First name of the instructor you want to create.")
  @NotEmpty(message = "First name must have a value.")
  private String firstName;

  @ApiModelProperty(value = "Last name of the instructor you want to create.")
  @NotEmpty(message = "Last name must have a value.")
  private String lastName;

  @ApiModelProperty(value = "Gender of the instructor you want to create.",
      allowableValues = "MALE, FEMALE")
  private Gender gender;

  @ApiModelProperty(value = "Date of birth of the instructor you want to create.")
  @Past(message = "Date of birth must be in the past.")
  private LocalDate dateOfBirth;

  @ApiModelProperty(value = "Mailing address of the instructor you want to create.")
  @NotEmpty(message = "Address must have a value.")
  private String mailingAddress;

  @ApiModelProperty(value = "Billing address of the instructor you want to create.")
  private String billingAddress;

  @ApiModelProperty(value = "Email address of the instructor you want to create.")
  @Email(message = "Email should be valid.")
  @NotEmpty(message = "Email must have a value.")
  private String email;

  @ApiModelProperty(value = "Phone number of the instructor you want to create.")
  @NotNull(message = "Phone number must not be null.")
  @Pattern(regexp = "([0-9\\s\\-+]{7,})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$",
      message = "Phone number is invalid.")
  private String phoneNumber;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt = LocalDateTime.now();

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime lastUpdatedAt = LocalDateTime.now();

  public Instructor convertToInstructor() {
    return new Instructor(this.id, this.firstName, this.lastName, this.gender, this.dateOfBirth,
        this.mailingAddress, this.billingAddress, this.email, this.phoneNumber,
        this.createdAt, this.lastUpdatedAt);
  }
}
