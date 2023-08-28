package com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos;

import com.greenfoxacademy.vocseikatimasterwork.models.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentForCourseDto {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String level;

  public static StudentForCourseDto convertToStudentForCourseDto(Student student) {
    return new StudentForCourseDto(student.getId(), student.getFirstName(), student.getLastName(),
        student.getEmail(), student.getPhoneNumber(), student.getLevel());
  }
}
