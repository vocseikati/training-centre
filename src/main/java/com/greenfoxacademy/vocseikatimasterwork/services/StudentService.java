package com.greenfoxacademy.vocseikatimasterwork.services;

import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CoursesDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.CreateStudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.UpdateStudentDto;
import java.util.List;

public interface StudentService {

  StudentDto findStudentById(Long id);

  List<StudentDto> listAllStudents();

  StudentDto addStudent(CreateStudentDto studentDto);

  StudentDto removeStudent(Long id);

  StudentDto modifyStudent(UpdateStudentDto student, Long idOfOriginalStudent);

  CoursesDto getCoursesOfStudent(Long studentId, String status);
}
