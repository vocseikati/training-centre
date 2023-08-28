package com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.greenfoxacademy.vocseikatimasterwork.models.CourseType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "Details of the course you want to update.")
public class UpdateCourseDto {

  @ApiModelProperty(value = "ID of the course.")
  private Long id;

  @ApiModelProperty(value = "Title of the course you want to update.")
  private String title;

  @ApiModelProperty(value = "Start date of the course you want to update.")
  @NotNull(message = "Start date must have a value.")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @ApiModelProperty(value = "End date of the course you want to update.")
  @NotNull(message = "Start date must have a value.")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  @ApiModelProperty(value = "The length of the course in hours you want to update.")
  private Integer durationInHours;

  @ApiModelProperty(value = "Setup the order of the course, individual or group.")
  private Boolean individualEducation;

  @ApiModelProperty(value = "Price of the course you want to update.")
  private Double price;

  @ApiModelProperty(value = "Type of the course you want to update.")
  private CourseType type;

  @ApiModelProperty(value = "Unique identifier of the instructor who is leading the course.")
  private Long instructorId;

  @ApiModelProperty(value = "Unique identifier of the room where is being leading the course.")
  @NotNull(message = "Classroom ID must not be null.")
  @Positive(message = "Classroom ID must be positive.")
  private Long classRoomId;
}
