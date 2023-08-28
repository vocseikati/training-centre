package com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos;

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
@ApiModel(discriminator = "Data of the instructor you want to update.")
public class UpdateInstructorDto {

  @ApiModelProperty(value = "ID of the instructor.")
  private Long id;

  @ApiModelProperty(value = "First name of the instructor you want to update.")
  private String firstName;

  @ApiModelProperty(value = "Last name of the instructor you want to update.")
  private String lastName;

  @ApiModelProperty(value = "Gender of the instructor you want to update.",
      allowableValues = "MALE, FEMALE")
  private Gender gender;

  @ApiModelProperty(value = "Date of birth of the instructor you want to update.")
  @Past(message = "Date of birth must be in the past.")
  private LocalDate dateOfBirth;

  @ApiModelProperty(value = "Mailing address of the instructor you want to update.")
  private String mailingAddress;

  @ApiModelProperty(value = "Billing address of the instructor you want to update.")
  private String billingAddress;

  @ApiModelProperty(value = "Email address of the instructor you want to update.")
  @Email(message = "Email should be valid.")
  private String email;

  @ApiModelProperty(value = "Phone number of the instructor you want to update.")
  @Pattern(regexp = "([0-9\\s\\-+]{7,})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$",
      message = "Phone number is invalid.")
  private String phoneNumber;
}
