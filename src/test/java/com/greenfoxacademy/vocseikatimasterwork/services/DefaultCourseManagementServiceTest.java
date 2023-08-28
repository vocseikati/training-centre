package com.greenfoxacademy.vocseikatimasterwork.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.EntityNotFoundException;
import com.greenfoxacademy.vocseikatimasterwork.models.CourseType;
import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentForCourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentsDto;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.ClassRoom;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Student;
import com.greenfoxacademy.vocseikatimasterwork.repositories.CourseRepository;
import com.greenfoxacademy.vocseikatimasterwork.repositories.StudentRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultCourseManagementServiceTest {
  @Mock
  private CourseRepository courseRepository;
  @Mock
  private StudentRepository studentRepository;
  @InjectMocks
  private DefaultCourseManagementService courseManagementService;

  private final List<Student> students = Arrays.asList(
      new Student(1L, "Test", "Test name", Gender.FEMALE,
          LocalDate.of(2000, 1, 1), "test address", "test address",
          "test@email.hu", "test phone number", "beginner",
          LocalDateTime.of(2021, 1, 1, 0, 0),
          LocalDateTime.of(2021, 1, 1, 0, 0)),
      new Student(2L, "Test2", "Test2 name", Gender.FEMALE,
          LocalDate.of(2000, 1, 1), "test2 address", "test2 address",
          "test2@email.hu", "test2 phone number", "test",
          LocalDateTime.of(2021, 1, 1, 0, 0),
          LocalDateTime.of(2021, 1, 1, 0, 0))
  );

  private final List<ClassRoom> rooms = Arrays.asList(
      new ClassRoom(1L, "theoretical room", "test room address", 10,
          LocalDateTime.of(2021, 1, 1, 0, 0),
          LocalDateTime.of(2021, 1, 1, 0, 0)),
      new ClassRoom(2L, "practical room", "test2 room address", 5,
          LocalDateTime.of(2021, 1, 1, 0, 0),
          LocalDateTime.of(2021, 1, 1, 0, 0))
  );

  private final List<Course> courses = Arrays.asList(
      new Course(1L, "Test title",
          LocalDate.now().minusMonths(1),
          LocalDate.now().minusDays(10), 40, false,
          100000, CourseType.THEORETICAL, Status.FINISHED, null, null,
          LocalDateTime.of(2021, 1, 1, 0, 0),
          LocalDateTime.of(2021, 1, 1, 0, 0)),
      new Course(2L, "Test2 title",
          LocalDate.now().plusDays(10),
          LocalDate.now().plusDays(20), 40, false,
          100000, CourseType.PRACTICAL, Status.PLANNED, null, null,
          LocalDateTime.of(2021, 1, 1, 0, 0),
          LocalDateTime.of(2021, 1, 1, 0, 0)),
      new Course(3L, "Test3 title",
          LocalDate.now().minusDays(10),
          LocalDate.now().plusDays(10), 40, true,
          100000, CourseType.PRACTICAL, Status.IN_PROGRESS, null, rooms.get(0),
          LocalDateTime.of(2021, 1, 1, 0, 0),
          LocalDateTime.of(2021, 1, 1, 0, 0))
  );

  @Test
  public void registerStudentToCourseWithNullCourseId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseManagementService.registerStudentToCourse(null, students.get(0).getId()));
    assertEquals("Please enter a course id.", e.getMessage());
  }

  @Test
  public void registerStudentToCourseWithInvalidStudentId_throwsException() {
    long studentId = 0L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseManagementService.registerStudentToCourse(courses.get(0).getId(), studentId));
    assertEquals("Invalid Student Id: " + studentId, e.getMessage());
  }

  @Test
  public void registerStudentToCourseWithNonExistentCourseId_throwsException() {
    long courseId = 1L;
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> courseManagementService.registerStudentToCourse(courseId, students.get(0).getId()));
    assertEquals(
        "404 NOT_FOUND \"Failed to register Student. Invalid Course Id : " + courseId + "\"",
        e.getMessage());
  }

  @Test
  public void registerStudentToCourseFinishedCourse_throwsException() {
    long courseId = 1L;
    long studentId = 1L;
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(courses.get(0)));
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseManagementService.registerStudentToCourse(courseId, studentId));
    assertEquals("Registration failed. This course has already been finished.", e.getMessage());
  }

  @Test
  public void registerStudentToCourseWithNonExistentStudentId_throwsException() {
    long studentId = 1L;
    long courseId = 2L;
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
    when(courseRepository.findById(courseId))
        .thenReturn(Optional.of(courses.get((int) (courseId - 1))));
    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> courseManagementService.registerStudentToCourse(courseId, studentId));
    assertEquals("404 NOT_FOUND \"Student with id: " + studentId
            + " cannot be found, please use adding student first.\"",
        e.getMessage());
  }

  @Test
  public void registerStudentToCourseAlreadyRegisteredStudent_throwsException() {
    long studentId = 1L;
    long courseId = 2L;
    Student student = students.get(0);
    Course course = courses.get((int) (courseId - 1));
    course.getStudents().add(student);

    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseManagementService.registerStudentToCourse(courseId, studentId));
    assertEquals("Student with id: " + studentId
        + " has already been registered for this course.", e.getMessage());
  }

  @Test
  public void registerStudentToCourse_WorksCorrectly() {
    long studentId = 1L;
    long courseId = 3L;
    Student student = students.get(0);
    Course course = courses.get((int) (courseId - 1));

    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

    StudentsDto result = courseManagementService.registerStudentToCourse(courseId, studentId);

    course.getStudents().add(student);
    Set<StudentForCourseDto> expected = course.getStudents().stream()
        .map(StudentForCourseDto::convertToStudentForCourseDto)
        .collect(Collectors.toSet());

    assertEquals(new StudentsDto(expected), result);
  }

  @Test
  public void deleteStudentFromCourseWithNullStudentId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
        courseManagementService.deleteStudentFromCourse(courses.get(0).getId(), null));
    assertEquals("Please enter a student id.", e.getMessage());
  }

  @Test
  public void deleteStudentFromCourseWithNonExistentCourseId_throwsException() {
    long courseId = 2L;
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> courseManagementService.deleteStudentFromCourse(courseId, students.get(0).getId()));
    assertEquals("404 NOT_FOUND \"Course with id: " + courseId + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  public void deleteStudentFromCourse_worksCorrectly() {
    long studentId = 1L;
    long courseId = 2L;
    Student student = students.get(0);
    Student student2 = students.get((int) (studentId));
    Course course = courses.get((int) (courseId - 1));
    Set<Student> students = course.getStudents();
    students.add(student);
    students.add(student2);

    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

    when(studentRepository.save(any(Student.class))).thenReturn(student);
    when(courseRepository.save(any(Course.class))).thenReturn(course);

    courseManagementService.deleteStudentFromCourse(courseId, studentId);
    assertEquals(students.size(), 1);
  }

  @Test
  public void setCompleteCourseAndMakeCertificatesWithNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseManagementService.setCompleteCourseAndMakeCertificates(null));
    assertEquals("Please enter a course id.", e.getMessage());
  }

  @Test
  public void setCompleteCourseAndMakeCertificatesWithNonExistentCourseId_throwsException() {
    long courseId = 1L;
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> courseManagementService.setCompleteCourseAndMakeCertificates(courseId));
    assertEquals(
        "404 NOT_FOUND \"Course with id: " + courseId + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  public void setCompleteCourseAndMakeCertificatesWithInProgressCourse_throwsException() {
    long courseId = 3L;
    when(courseRepository.findById(courseId))
        .thenReturn(Optional.of(courses.get((int) (courseId - 1))));
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseManagementService.setCompleteCourseAndMakeCertificates(courseId));
    assertEquals("Course is still in progress, or planned.", e.getMessage());
  }
}
