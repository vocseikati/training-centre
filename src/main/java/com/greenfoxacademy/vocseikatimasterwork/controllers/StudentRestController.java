package com.greenfoxacademy.vocseikatimasterwork.controllers;

import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.isNumeric;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNonNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.validateBindingResult;

import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CoursesDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.CreateStudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.UpdateStudentDto;
import com.greenfoxacademy.vocseikatimasterwork.services.StudentService;
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
public class StudentRestController {

  private final StudentService studentService;

  @Autowired
  public StudentRestController(StudentService studentService) {
    this.studentService = studentService;
  }

  @GetMapping("/students")
  @ApiOperation(value = "Lists all students.")
  public ResponseEntity<List<StudentDto>> listStudents() {
    return ResponseEntity.ok(studentService.listAllStudents());
  }

  @PostMapping("/students")
  @ApiOperation(value = "Saves a new student to the database.")
  public ResponseEntity<StudentDto> createStudent(
      @ApiParam(value = "The data of the student that you want to save.", required = true)
      @RequestBody(required = false) @Valid CreateStudentDto student,
      BindingResult bindingResult
  ) {
    requireNonNull(student, "Student must not be null.");
    validateBindingResult(bindingResult);

    StudentDto createdStudent = studentService.addStudent(student);

    URI location = URI.create(String.format("/api/ohaj/students/%s", createdStudent.getId()));
    return ResponseEntity.created(location).body(createdStudent);
  }

  @GetMapping("/students/{id}")
  @ApiOperation(value = "Fetches and returns a student by id.")
  public ResponseEntity<StudentDto> findStudentById(
      @ApiParam(value = "Id of the required student.", required = true, type = "Integer as string")
      @PathVariable String id
  ) {
    requireNonNull(id, "Id must not be null.");
    return ResponseEntity.ok().body(studentService.findStudentById(isNumeric(id)));
  }

  @DeleteMapping("/students/{id}")
  @ApiOperation(value = "Deletes a student from the database.")
  public ResponseEntity<StudentDto> deleteStudentById(
      @ApiParam(value = "Id of the student that you want to delete.", required = true,
          type = "Integer as string")
      @PathVariable String id
  ) {
    requireNonNull(id, "Id must not be null.");

    return ResponseEntity.ok().body(studentService.removeStudent(isNumeric(id)));
  }

  @PutMapping("/students/{id}")
  @ApiOperation(value = "Updates an existing student in the database.")
  public ResponseEntity<StudentDto> updateStudent(
      @ApiParam(value = "Id of the student you want to update.", required = true,
          type = "Integer as string")
      @PathVariable String id,

      @ApiParam(value = "The student object that you want to update the original with",
          required = true)
      @RequestBody(required = false) @Valid UpdateStudentDto student,
      BindingResult bindingResult
  ) {
    requireNonNull(id, "Id must not be null.");
    requireNonNull(student, "Student must not be null.");
    validateBindingResult(bindingResult);

    studentService.modifyStudent(student, isNumeric(id));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/coursesOfStudent/{studentId}")
  @ApiOperation(value = "List of courses belonging to the student specified "
      + "by the identification number, can be filtered based on the status of the course.")
  public ResponseEntity<CoursesDto> getCoursesOfStudentById(
      @ApiParam(value = "Id of the student whose courses you want to list.", required = true,
          type = "Integer as string")
      @PathVariable String studentId,

      @ApiParam(value = "Status of the course that you want to list.",
          allowableValues = "finished, in_progress, planned")
      @RequestParam(required = false) String status
  ) {
    requireNonNull(studentId, "Id must not be null.");

    if (status == null || status.isEmpty()) {
      CoursesDto allCoursesOfStudent =
          studentService.getCoursesOfStudent(isNumeric(studentId), null);
      return ResponseEntity.ok().body(allCoursesOfStudent);
    }

    return ResponseEntity.ok()
        .body(studentService.getCoursesOfStudent(isNumeric(studentId), status));
  }
}
