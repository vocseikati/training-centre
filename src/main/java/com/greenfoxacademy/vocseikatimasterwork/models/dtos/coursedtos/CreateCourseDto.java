package com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.greenfoxacademy.vocseikatimasterwork.models.CourseType;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.ClassRoom;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Instructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "Details of the course you want to create.")
public class CreateCourseDto {

  @ApiModelProperty(value = "ID of the course.")
  @Positive(message = "ID must be positive (0: invalid).")
  private Long id;

  @ApiModelProperty(value = "Title of the course you want to create.")
  @NotBlank(message = "Title must have a value.")
  private String title;

  @ApiModelProperty(value = "Start date of the course you want to create.")
  @NotNull(message = "Start date must have a value.")
  @FutureOrPresent(message = "Course start date must be in the future.")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @ApiModelProperty(value = "End date of the course you want to create.")
  @NotNull(message = "Start date must have a value.")
  @FutureOrPresent(message = "Course end date must be in the future.")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  @ApiModelProperty(value = "The length of the course in hours you want to create.")
  @NotNull(message = "Duration must have a value.")
  @Positive(message = "Duration hours must be positive.")
  private Integer durationInHours;

  @ApiModelProperty(value = "Setup the order of the course, individual or group.")
  @NotNull(message = "Education format must have a value. Allowed input: true or false.")
  private Boolean individualEducation;

  @ApiModelProperty(value = "Price of the course you want to create.")
  @NotNull(message = "Price must be set.")
  private Double price;

  @ApiModelProperty(value = "Type of the course you want to create.")
  private CourseType type;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Status status;

  @ApiModelProperty(value = "Unique identifier of the instructor who is leading the course.")
  private Long instructorId;

  @ApiModelProperty(value = "Unique identifier of the room where is being leading the course.")
  @NotNull(message = "Classroom ID must not be null.")
  @Positive(message = "Classroom ID must be positive.")
  private Long classRoomId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt = LocalDateTime.now();

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime lastUpdatedAt = LocalDateTime.now();

  public Course convertToCourse(Instructor instructor, ClassRoom classRoom) {
    return new Course(this.id, this.title, this.startDate, this.endDate, this.durationInHours,
        this.individualEducation, this.price, this.type, this.status,
        findInstructor(instructor), classRoom, this.createdAt, this.lastUpdatedAt);
  }

  public Status getStatus() {
    if (this.endDate.isBefore(LocalDate.now())) {
      this.status = Status.FINISHED;
    }
    if (this.endDate.isAfter(LocalDate.now()) && this.startDate.isBefore(LocalDate.now())) {
      this.status = Status.IN_PROGRESS;
    }
    if (this.startDate.isAfter(LocalDate.now())) {
      this.status = Status.PLANNED;
    }
    return status;
  }

  private void setStatus() {
    if (this.endDate.isBefore(LocalDate.now())) {
      this.status = Status.FINISHED;
    }
    if (this.startDate.isBefore(LocalDate.now()) && this.endDate.isAfter(LocalDate.now())) {
      this.status = Status.IN_PROGRESS;
    }
    this.status = Status.PLANNED;
  }

  private Instructor findInstructor(Instructor instructor) {
    if (this.instructorId == null) {
      instructor = null;
    }
    return instructor;
  }
}
