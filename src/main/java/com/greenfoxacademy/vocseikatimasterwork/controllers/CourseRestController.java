package com.greenfoxacademy.vocseikatimasterwork.controllers;

import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.isNumeric;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNonNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.validateBindingResult;

import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CreateCourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.UpdateCourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentsDto;
import com.greenfoxacademy.vocseikatimasterwork.services.CourseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ohaj")
public class CourseRestController { //todo:exceptionhandling

  private final CourseService courseService;

  @Autowired
  public CourseRestController(CourseService courseService) {
    this.courseService = courseService;
  }

  @GetMapping("/courses")
  @ApiOperation(value = "Lists all courses, can be filtered based on the status of the course.")
  public ResponseEntity<List<CourseDto>> listCourses(
      @ApiParam(value = "Status of the course that you want to list.",
          allowableValues = "finished, in_progress, planned")
      @RequestParam(required = false) String status
  ) {
    if (status == null || status.isEmpty()) {
      return ResponseEntity.ok(courseService.listAllCourses(null));
    }

    return ResponseEntity.ok(courseService.listAllCourses(status));
  }

  @PostMapping("/courses")
  @ApiOperation(value = "Saves a new course to the database.")
  public ResponseEntity<CourseDto> createCourse(
      @ApiParam(value = "The data of the course that you want to save.")
      @RequestBody(required = false) @Valid CreateCourseDto course,
      BindingResult bindingResult
  ) {
    requireNonNull(course, "Course must not be null.");
    validateBindingResult(bindingResult);

    CourseDto createdCourse = courseService.addCourse(course);

    URI location = URI.create(String.format("/api/ohaj/courses/%s", createdCourse.getId()));
    return ResponseEntity.created(location).body(createdCourse);
  }

  @GetMapping("/courses/{id}")
  @ApiOperation(value = "Fetches and returns a course by id.")
  public ResponseEntity<CourseDto> findCourseById(
      @ApiParam(value = "Id of the required course.", required = true, type = "Integer as string")
      @PathVariable String id
  ) {
    requireNonNull(id, "Id must not be null.");
    return ResponseEntity.ok().body(courseService.findCourseById(isNumeric(id)));
  }

  @DeleteMapping("/courses/{id}")
  @ApiOperation(value = "Deletes a course from the database.")
  public ResponseEntity<CourseDto> deleteCourseById(
      @ApiParam(value = "Id of the course that you want to delete.", required = true,
          type = "Integer as string")
      @PathVariable String id
  ) {
    requireNonNull(id, "Id must not be null.");

    return ResponseEntity.ok().body(courseService.removeCourse(isNumeric(id)));
  }

  @PutMapping("/courses/{id}")
  @ApiOperation(value = "Updates an existing course in the database.")
  public ResponseEntity<CourseDto> updateCourse(
      @ApiParam(value = "Id of the course you want to update.", required = true,
          type = "Integer as string")
      @PathVariable String id,

      @ApiParam(value = "The course object that you want to update the original with.",
          required = true)
      @RequestBody(required = false) @Valid UpdateCourseDto course,
      BindingResult bindingResult
  ) {
    requireNonNull(id, "Id must not be null.");
    requireNonNull(course, "Course must not be null.");
    validateBindingResult(bindingResult);

    courseService.modifyCourse(course, isNumeric(id));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/studentsOfCourse/{courseId}")
  @ApiOperation(value = "List of students belonging to the course specified "
      + "by the identification number.")
  public ResponseEntity<StudentsDto> getStudentsOfCourseById(
      @ApiParam(value = "Id of the course whose students you want to list.", required = true,
          type = "Integer as string")
      @PathVariable String courseId
  ) {
    requireNonNull(courseId, "Id must not be null.");

    StudentsDto studentsOfCourse = courseService.getStudentsOfCourse(isNumeric(courseId));
    return ResponseEntity.ok().body(studentsOfCourse);
  }
}
