package com.greenfoxacademy.vocseikatimasterwork.controllers;

import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.isNumeric;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNonNull;

import com.greenfoxacademy.vocseikatimasterwork.models.dtos.certificatesdtos.Certificates;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentsDto;
import com.greenfoxacademy.vocseikatimasterwork.services.CourseManagementService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ohaj")
public class CourseManagementRestController {

  private final CourseManagementService courseManagementService;

  @Autowired
  public CourseManagementRestController(CourseManagementService courseManagementService) {
    this.courseManagementService = courseManagementService;
  }

  @PutMapping("/registerStudentToCourse/{courseId}")
  @ApiOperation(value = "Student applies to a course.")
  public ResponseEntity<StudentsDto> enrollStudentToCourse(
      @ApiParam(value = "Id of the course to which the student is applying.", required = true,
          type = "Integer as string")
      @PathVariable String courseId,
      @ApiParam(value = "Id of the student who is applying for the given course", required = true,
          type = "Integer as string")
      @RequestParam(required = false) String studentId
  ) {

    requireNonNull(courseId, "Course Id must not be null.");
    requireNonNull(studentId, "Student Id must not be null.");

    StudentsDto studentsDto =
        courseManagementService.registerStudentToCourse(isNumeric(courseId), isNumeric(studentId));

    return ResponseEntity.accepted().body(studentsDto);
  }

  @DeleteMapping("/deleteStudentFromCourse/{courseId}")
  @ApiOperation(value = "Instructor or Administrator deletes a student from the course.")
  public ResponseEntity<Object> removeStudentFromCourse(
      @ApiParam(value = "Id of the course from which the student is deleting.", required = true,
          type = "Integer as string")
      @PathVariable String courseId,

      @ApiParam(value = "Id of the student who is deleting from the course.", required = true,
          type = "Integer as string")
      @RequestParam(required = false) String studentId
  ) {
    requireNonNull(courseId, "Course Id must not be null.");
    requireNonNull(studentId, "Student Id must not be null.");

    courseManagementService.deleteStudentFromCourse(isNumeric(courseId), isNumeric(studentId));
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/finishCourse/{courseId}")
  @ApiOperation(value = "Administrator sets finished the course,"
      + " and makes certificates for students.")
  public ResponseEntity<Certificates> setFinishedCourse(
      @ApiParam(value = "Id of the course that you want to set finished.", required = true,
          type = "Integer as string")
      @PathVariable String courseId
  ) {
    requireNonNull(courseId, "Id must not be null.");
    Certificates certificates =
        courseManagementService.setCompleteCourseAndMakeCertificates(isNumeric(courseId));
    return ResponseEntity.ok().body(certificates);
  }
}
