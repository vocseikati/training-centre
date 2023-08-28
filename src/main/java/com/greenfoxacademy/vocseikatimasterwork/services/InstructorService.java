package com.greenfoxacademy.vocseikatimasterwork.services;

import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CoursesDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.CreateInstructorDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.InstructorDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.UpdateInstructorDto;
import java.util.List;

public interface InstructorService {

  InstructorDto findInstructorById(Long id);

  List<InstructorDto> listAllInstructor();

  InstructorDto addInstructor(CreateInstructorDto instructor);

  InstructorDto removeInstructor(Long id);

  InstructorDto modifyInstructor(UpdateInstructorDto instructor, Long idOfOriginalInstructor);

  CoursesDto getCoursesOfInstructor(Long instructorId, String inputStatus);
}
