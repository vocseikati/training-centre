package com.greenfoxacademy.vocseikatimasterwork.services;

import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CreateCourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.UpdateCourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentsDto;
import java.util.List;

public interface CourseService {

  CourseDto findCourseById(Long id);

  List<CourseDto> listAllCourses(String status);

  CourseDto addCourse(CreateCourseDto courseDto);

  CourseDto removeCourse(Long id);

  CourseDto modifyCourse(UpdateCourseDto course, Long idOfOriginalCourse);

  StudentsDto getStudentsOfCourse(Long courseId);

}
