package com.greenfoxacademy.vocseikatimasterwork.services;

import com.greenfoxacademy.vocseikatimasterwork.models.dtos.certificatesdtos.Certificates;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentsDto;

public interface CourseManagementService {

  StudentsDto registerStudentToCourse(Long courseId, Long studentId);

  void deleteStudentFromCourse(Long courseId, Long studentId);

  Certificates setCompleteCourseAndMakeCertificates(Long courseId);
}
