package com.greenfoxacademy.vocseikatimasterwork.services;

import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNonNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.validateLongId;

import com.greenfoxacademy.vocseikatimasterwork.aspests.CourseOperation;
import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.EntityNotFoundException;
import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.UnavailableRoomException;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.certificatesdtos.Certificates;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentForCourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentsDto;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Student;
import com.greenfoxacademy.vocseikatimasterwork.repositories.CourseRepository;
import com.greenfoxacademy.vocseikatimasterwork.repositories.StudentRepository;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultCourseManagementService implements CourseManagementService {

  private final CourseRepository courseRepository;
  private final StudentRepository studentRepository;
  private final CertificateService certificateService;

  @Autowired
  public DefaultCourseManagementService(CourseRepository courseRepository,
                                        StudentRepository studentRepository,
                                        CertificateService certificateService) {
    this.courseRepository = courseRepository;
    this.studentRepository = studentRepository;
    this.certificateService = certificateService;
  }

  @CourseOperation
  @Override
  public StudentsDto registerStudentToCourse(Long courseId, Long studentId) {
    requireNonNull(courseId, "Please enter a course id.");
    requireNonNull(studentId, "Please enter a student id.");
    validateLongId(courseId, "Invalid Course Id: " + courseId);
    validateLongId(studentId, "Invalid Student Id: " + studentId);

    Course course = courseRepository.findById(courseId).orElseThrow(
        () -> new EntityNotFoundException(
            "Failed to register Student. Invalid Course Id : " + courseId));

    if (course.getStatus().equals(Status.FINISHED)) {
      throw new IllegalArgumentException(
          "Registration failed. This course has already been finished.");
    }

    Student ownStudent = studentRepository.findById(studentId)
        .orElseThrow(() -> new EntityNotFoundException("Student with id: " + studentId
            + " cannot be found, please use adding student first."));

    Set<Student> courseStudents = course.getStudents();

    if (courseStudents.contains(ownStudent)) {
      throw new IllegalArgumentException(
          "Student with id: " + studentId + " has already been registered for this course.");
    }

    if (course.getIndividualEducation() && courseStudents.size() > 0) {
      throw new UnavailableRoomException("Room with Id: " + course.getClassRoom().getId()
          + " is unavailable. The course is individual education, and it is already booked.");
    }

    if (!checkIfRoomIsAvailable(course)) {
      throw new UnavailableRoomException("Room with Id: " + course.getClassRoom().getId()
          + " is unavailable. The room has reached its maximum capacity");
    }

    courseStudents.add(ownStudent);
    courseRepository.save(course);

    ownStudent.getCourses().add(course);
    studentRepository.save(ownStudent);

    Set<StudentForCourseDto> studentDtoSet = courseStudents.stream()
        .map(StudentForCourseDto::convertToStudentForCourseDto)
        .collect(Collectors.toSet());

    return new StudentsDto(studentDtoSet);
  }

  @CourseOperation
  @Override
  public void deleteStudentFromCourse(Long courseId, Long studentId) {
    requireNonNull(courseId, "Please enter a course id.");
    requireNonNull(studentId, "Please enter a student id.");
    validateLongId(courseId, "Invalid Course Id: " + courseId);
    validateLongId(studentId, "Invalid Student Id: " + studentId);

    Course course = courseRepository.findById(courseId).orElseThrow(
        () -> new EntityNotFoundException("Course with id: " + courseId + " cannot be found."));

    Student studentToRemove = studentRepository.findById(studentId).orElseThrow(
        () -> new EntityNotFoundException("Student with id: " + studentId + " cannot be found."));

    Set<Student> courseStudents = course.getStudents();
    if (!courseStudents.contains(studentToRemove)) {
      throw new IllegalArgumentException("This student does not participate in the given course.");
    }

    courseStudents.remove(studentToRemove);

    studentRepository.save(studentToRemove);
    courseRepository.save(course);
  }

  @CourseOperation
  @Override
  public Certificates setCompleteCourseAndMakeCertificates(Long courseId) {
    requireNonNull(courseId, "Please enter a course id.");
    validateLongId(courseId, "Invalid Course Id: " + courseId);

    Course course = courseRepository.findById(courseId).orElseThrow(
        () -> new EntityNotFoundException("Course with id: " + courseId + " cannot be found."));

    if (course.getEndDate().isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Course is still in progress, or planned.");
    }

    course.setStatus(Status.FINISHED);

    return certificateService.editCertificate(course);
  }

  private boolean checkIfRoomIsAvailable(Course course) {
    Integer capacity = course.getClassRoom().getCapacity();
    Set<Student> students = course.getStudents();
    return capacity > students.size();
  }
}
