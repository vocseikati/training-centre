package com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Instructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(discriminator = "Data of an instructor.")
public class InstructorDto {

  @ApiModelProperty(value = "Unique ID of the instructor, autogenerated by the database.")
  private Long id;

  @ApiModelProperty(value = "First name of the instructor.")
  private String firstName;

  @ApiModelProperty(value = "Last name of the instructor.")
  private String lastName;

  @ApiModelProperty(value = "Gender of the instructor.", allowableValues = "MALE, FEMALE")
  private Gender gender;

  @ApiModelProperty(value = "Date of birth of the instructor.")
  private LocalDate dateOfBirth;

  @ApiModelProperty(value = "Mailing address of the instructor.")
  private String mailingAddress;

  @ApiModelProperty(value = "Billing address of the instructor.")
  private String billingAddress;

  @ApiModelProperty(value = "Email address of the instructor.")
  private String email;

  @ApiModelProperty(value = "Phone number of the instructor.")
  private String phoneNumber;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime lastUpdatedAt;

  public static InstructorDto convertToInstructorDto(Instructor instructor) {
    return new InstructorDto(instructor.getId(), instructor.getFirstName(),
        instructor.getLastName(), instructor.getGender(), instructor.getDateOfBirth(),
        instructor.getMailingAddress(), instructor.getBillingAddress(), instructor.getEmail(),
        instructor.getPhoneNumber(), instructor.getCreatedAt(), instructor.getLastUpdatedAt());
  }
}