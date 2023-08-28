package com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos;

import com.greenfoxacademy.vocseikatimasterwork.models.CourseType;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseForParticipantDto {

  private Long id;
  private String title;
  private LocalDate startDate;
  private LocalDate endDate;
  private Integer durationInHours;
  private Boolean individualEducation;
  private Double price;
  private CourseType type;
  private Status status;
  private Long instructorId;
  private Long roomId;

  public static CourseForParticipantDto convertToCourseForParticipantDto(Course course) {
    return new CourseForParticipantDto(course.getId(), course.getTitle(), course.getStartDate(),
        course.getEndDate(), course.getDurationInHours(), course.getIndividualEducation(),
        course.getPrice(), course.getType(), course.getStatus(), getInstructorId(course),
        course.getClassRoom().getId());
  }

  private static Long getInstructorId(Course course) {
    if (course.getInstructor() == null) {
      return null;
    }
    return course.getInstructor().getId();
  }
}
