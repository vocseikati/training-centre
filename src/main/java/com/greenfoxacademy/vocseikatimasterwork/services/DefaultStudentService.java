package com.greenfoxacademy.vocseikatimasterwork.services;

import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNonNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.validateLongId;

import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.EntityNotFoundException;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CourseForParticipantDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CoursesDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.CreateStudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.UpdateStudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Student;
import com.greenfoxacademy.vocseikatimasterwork.repositories.StudentRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultStudentService implements StudentService {

  private final StudentRepository studentRepository;

  @Autowired
  public DefaultStudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  @Override
  public StudentDto findStudentById(Long id) {
    requireNonNull(id, "Please enter an id.");
    validateLongId(id, "Invalid Student Id: " + id);

    Student student = studentRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Student with id: " + id + " cannot be found."));

    return StudentDto.convertToStudentDto(student);
  }

  @Override
  public List<StudentDto> listAllStudents() {
    return studentRepository.findAll().stream()
        .map(StudentDto::convertToStudentDto)
        .collect(Collectors.toList());
  }

  @Override
  public StudentDto addStudent(CreateStudentDto student) {
    requireNonNull(student, "The request body is empty, you must send a valid request.");
    requireNull(student.getId(), "Student id must be null.");

    if (studentRepository.existsStudentByEmail(student.getEmail())) {
      throw new IllegalArgumentException(
          "Email address is already registered. Student email address must be unique.");
    }

    Student studentSaved = studentRepository.save(student.convertToStudent());
    return StudentDto.convertToStudentDto(studentSaved);
  }

  @Override
  public StudentDto removeStudent(Long id) {
    requireNonNull(id, "Please enter an id.");
    validateLongId(id, "Invalid Student Id: " + id);

    Student student = studentRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Student with id: " + id + " cannot be found."));

    if (!student.getCourses().isEmpty()) {
      throw new IllegalArgumentException("Student has already been registered to courses. Please,"
          + " delete this student from his/her course first.");
    }

    studentRepository.deleteById(id);
    return StudentDto.convertToStudentDto(student);
  }

  @Override
  public StudentDto modifyStudent(UpdateStudentDto student, Long idOfOriginalStudent) {
    requireNonNull(student, "The request body is empty, you must send a valid request.");
    requireNonNull(idOfOriginalStudent, "Id of original student is null. Please enter an id.");
    validateLongId(idOfOriginalStudent, "Invalid Student Id: " + idOfOriginalStudent);

    Student originalStudent = studentRepository.findById(idOfOriginalStudent)
        .orElseThrow(() -> new EntityNotFoundException(
            "Id of original student: " + idOfOriginalStudent + " cannot be found."));

    if (student.getFirstName() != null && !student.getFirstName().isEmpty()) {
      originalStudent.setFirstName(student.getFirstName());
    }
    if (student.getLastName() != null && !student.getLastName().isEmpty()) {
      originalStudent.setLastName(student.getLastName());
    }
    if (student.getGender() != null) {
      originalStudent.setGender(student.getGender());
    }
    if (student.getDateOfBirth() != null) {
      originalStudent.setDateOfBirth(student.getDateOfBirth());
    }
    if (student.getMailingAddress() != null && !student.getMailingAddress().isEmpty()) {
      originalStudent.setMailingAddress(student.getMailingAddress());
    }
    if (student.getBillingAddress() != null && !student.getBillingAddress().isEmpty()) {
      originalStudent.setBillingAddress(student.getBillingAddress());
    }
    if (student.getEmail() != null && !student.getEmail().isEmpty()) {
      if (!originalStudent.getEmail().equals(student.getEmail())
          && studentRepository.existsStudentByEmail(student.getEmail())) {
        throw new IllegalArgumentException(
            "Email address is already registered. Student email address must be unique.");
      }
      originalStudent.setEmail(student.getEmail());
    }
    if (student.getPhoneNumber() != null && !student.getPhoneNumber().isEmpty()) {
      originalStudent.setPhoneNumber(student.getPhoneNumber());
    }
    if (student.getLevel() != null && !student.getLevel().isEmpty()) {
      originalStudent.setLevel(student.getLevel());
    }

    studentRepository.save(originalStudent);
    return StudentDto.convertToStudentDto(originalStudent);
  }


  @Override
  public CoursesDto getCoursesOfStudent(Long studentId, String inputStatus) {
    requireNonNull(studentId, "Please enter a student id.");
    validateLongId(studentId, "Invalid Student Id: " + studentId);

    Student student = studentRepository.findById(studentId).orElseThrow(
        () -> new EntityNotFoundException("Student with id: " + studentId + " cannot be found."));
    Set<Course> courses = student.getCourses();

    if (inputStatus == null || inputStatus.isEmpty()) {
      Set<CourseForParticipantDto> courseDtoSet = courses.stream()
          .map(CourseForParticipantDto::convertToCourseForParticipantDto)
          .collect(Collectors.toSet());
      return new CoursesDto(courseDtoSet);
    }
    try {
      Status.valueOf(inputStatus.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Status must be from: " + Arrays.asList(Status.values()));
    }

    Set<Course> coursesByStatus = new HashSet<>();
    for (Course course : courses) {
      if (course.getStatus().equals(Status.valueOf(inputStatus.toUpperCase()))) {
        coursesByStatus.add(course);
      }
    }
    Set<CourseForParticipantDto> coursesByStatusDtoSet = coursesByStatus.stream()
        .map(CourseForParticipantDto::convertToCourseForParticipantDto)
        .collect(Collectors.toSet());
    return new CoursesDto(coursesByStatusDtoSet);
  }
}
