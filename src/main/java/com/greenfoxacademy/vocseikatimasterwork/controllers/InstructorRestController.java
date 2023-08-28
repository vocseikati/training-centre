package com.greenfoxacademy.vocseikatimasterwork.controllers;

import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.isNumeric;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNonNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.validateBindingResult;

import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CoursesDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.CreateInstructorDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.InstructorDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.UpdateInstructorDto;
import com.greenfoxacademy.vocseikatimasterwork.services.InstructorService;
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
public class InstructorRestController {

  private final InstructorService instructorService;

  @Autowired
  public InstructorRestController(InstructorService instructorService) {
    this.instructorService = instructorService;
  }

  @GetMapping("/instructors")
  @ApiOperation(value = "Lists all instructors.")
  public ResponseEntity<List<InstructorDto>> listInstructors() {
    return ResponseEntity.ok(instructorService.listAllInstructor());
  }

  @PostMapping("/instructors")
  @ApiOperation(value = "Saves a new instructor to the database.")
  public ResponseEntity<InstructorDto> addInstructor(
      @ApiParam(value = "The data of the instructor that you want to save.", required = true)
      @RequestBody(required = false) @Valid CreateInstructorDto instructor,
      BindingResult bindingResult
  ) {
    requireNonNull(instructor, "Instructor must not be null.");
    validateBindingResult(bindingResult);

    InstructorDto createdInstructor = instructorService.addInstructor(instructor);

    URI location = URI.create(String.format("/api/ohaj/instructors/%s", createdInstructor.getId()));
    return ResponseEntity.created(location).body(createdInstructor);
  }

  @GetMapping("/instructors/{id}")
  @ApiOperation(value = "Fetches and returns an instructor by id.")
  public ResponseEntity<InstructorDto> findInstructorById(
      @ApiParam(value = "Id of the required instructor.", required = true,
          type = "Integer as string")
      @PathVariable String id
  ) {
    requireNonNull(id, "Id must not be null.");
    return ResponseEntity.ok().body(instructorService.findInstructorById(isNumeric(id)));
  }

  @DeleteMapping("/instructors/{id}")
  @ApiOperation(value = "Deletes an instructor from the database.")
  public ResponseEntity<InstructorDto> deleteInstructorById(
      @ApiParam(value = "Id of the instructor that you want to delete.", required = true,
          type = "Integer as string")
      @PathVariable String id
  ) {
    requireNonNull(id, "Id must not be null.");

    return ResponseEntity.ok().body(instructorService.removeInstructor(isNumeric(id)));
  }

  @PutMapping("/instructors/{id}")
  @ApiOperation(value = "Updates an existing instructor in the database.")
  public ResponseEntity<InstructorDto> updateInstructor(
      @ApiParam(value = "Id of the instructor you want to update.", required = true,
          type = "Integer as string")
      @PathVariable String id,

      @ApiParam(value = "The instructor object that you want to update the original with.",
          required = true)
      @RequestBody(required = false) @Valid UpdateInstructorDto instructor,
      BindingResult bindingResult
  ) {
    requireNonNull(id, "Id must not be null.");
    requireNonNull(instructor, "Instructor must not be null.");
    validateBindingResult(bindingResult);

    instructorService.modifyInstructor(instructor, isNumeric(id));
    return ResponseEntity.noContent().build();

  }

  @GetMapping("/coursesOfInstructor/{instructorId}")
  @ApiOperation(value = "List of courses belonging to the instructor specified "
      + "by the identification number, can be filtered based on the status of the course.")
  public ResponseEntity<CoursesDto> getCoursesOfInstructorById(
      @ApiParam(value = "Id of the instructor whose courses you want to list.", required = true,
          type = "Integer as string")
      @PathVariable String instructorId,
      @ApiParam(value = "Status of the course that you want to list.",
          allowableValues = "finished, in_progress, planned")
      @RequestParam(required = false) String status
  ) {
    requireNonNull(instructorId, "Id must not be null.");

    if (status == null || status.isEmpty()) {
      CoursesDto coursesOfInstructor =
          instructorService.getCoursesOfInstructor(isNumeric(instructorId), null);
      return ResponseEntity.ok().body(coursesOfInstructor);
    }

    return ResponseEntity.ok()
        .body(instructorService.getCoursesOfInstructor(isNumeric(instructorId), status));
  }
}
