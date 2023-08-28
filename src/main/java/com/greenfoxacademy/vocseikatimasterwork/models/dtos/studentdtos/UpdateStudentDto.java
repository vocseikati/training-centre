package com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos;

import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class UpdateStudentDto {

  @ApiModelProperty(value = "ID of the student.")
  private Long id;

  @ApiModelProperty(value = "First name of the student you want to update.")
  private String firstName;

  @ApiModelProperty(value = "Last name of the student you want to update.")
  private String lastName;

  @ApiModelProperty(value = "Gender of the student you want to update.",
      allowableValues = "MALE, FEMALE")
  private Gender gender;

  @ApiModelProperty(value = "Date of birth of the student you want to update.")
  @Past(message = "Date of birth must be in the past.")
  private LocalDate dateOfBirth;

  @ApiModelProperty(value = "Mailing address of the student you want to update.")
  private String mailingAddress;

  @ApiModelProperty(value = "Billing address of the student you want to update.")
  private String billingAddress;

  @ApiModelProperty(value = "Email address of the student you want to update.")
  @Email(message = "Email should be valid.")
  private String email;

  @ApiModelProperty(value = "Phone number of the student you want to update.")
  @Pattern(regexp = "([0-9\\s\\-+]{7,})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$",
      message = "Phone number is invalid.")
  private String phoneNumber;

  @ApiModelProperty(value = "Level of knowledge of the student you want to update.",
      example = "beginner, career modifier, master")
  private String level;

}
