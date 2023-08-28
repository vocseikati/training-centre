package com.greenfoxacademy.vocseikatimasterwork.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.EntityNotFoundException;
import com.greenfoxacademy.vocseikatimasterwork.models.CourseType;
import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CourseForParticipantDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CoursesDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.CreateStudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.UpdateStudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.ClassRoom;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Student;
import com.greenfoxacademy.vocseikatimasterwork.repositories.StudentRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultStudentServiceTest {

  @Mock
  private StudentRepository studentRepository;

  @InjectMocks
  private DefaultStudentService studentService;

  private List<Student> students;

  private CreateStudentDto createStudentDto;
  private UpdateStudentDto updateStudentDto;

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
          100000, CourseType.THEORETICAL, Status.FINISHED, null, rooms.get(0),
          LocalDateTime.of(2021, 1, 1, 0, 0),
          LocalDateTime.of(2021, 1, 1, 0, 0)),
      new Course(2L, "Test2 title",
          LocalDate.now().plusDays(10),
          LocalDate.now().plusDays(20), 40, false,
          100000, CourseType.PRACTICAL, Status.PLANNED, null, rooms.get(0),
          LocalDateTime.of(2021, 1, 1, 0, 0),
          LocalDateTime.of(2021, 1, 1, 0, 0)),
      new Course(3L, "Test3 title",
          LocalDate.now().minusDays(10),
          LocalDate.now().plusDays(10), 40, true,
          100000, CourseType.PRACTICAL, Status.IN_PROGRESS, null, rooms.get(1),
          LocalDateTime.of(2021, 1, 1, 0, 0),
          LocalDateTime.of(2021, 1, 1, 0, 0))
  );

  @BeforeEach
  void setUp() {

    students = Arrays.asList(
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

    createStudentDto = new CreateStudentDto();
    createStudentDto.setFirstName("create test");
    createStudentDto.setLastName("post test");
    createStudentDto.setGender(Gender.FEMALE);
    createStudentDto.setDateOfBirth(LocalDate.of(2000, 1, 1));
    createStudentDto.setMailingAddress("post address");
    createStudentDto.setBillingAddress("post address");
    createStudentDto.setEmail("post email");
    createStudentDto.setPhoneNumber("post phone number");

    updateStudentDto = new UpdateStudentDto();
    updateStudentDto.setId(1L);
    updateStudentDto.setFirstName("put test");
    updateStudentDto.setLastName("put test");
    updateStudentDto.setGender(Gender.FEMALE);
    updateStudentDto.setDateOfBirth(LocalDate.of(2000, 1, 1));
    updateStudentDto.setMailingAddress("put test address");
    updateStudentDto.setBillingAddress("put test address");
    updateStudentDto.setEmail("put test email");
    updateStudentDto.setPhoneNumber("put test phone number");
  }

  @Test
  public void listAllStudent_thenReturnsCorrectListOfDTOs() {
    when(studentRepository.findAll()).thenReturn(students);

    List<StudentDto> actual = students.stream()
        .map(StudentDto::convertToStudentDto).collect(Collectors.toList());

    List<StudentDto> result = studentService.listAllStudents();

    assertEquals(2, result.size());
    assertEquals(result, actual);
  }

  @Test
  public void findStudentById_returnsDtoCorrectly() {
    when(studentRepository.findById(1L)).thenReturn(Optional.of(students.get(0)));
    StudentDto actual = studentService.findStudentById(1L);
    StudentDto expected = StudentDto.convertToStudentDto(students.get(0));
    assertEquals(expected, actual);
  }

  @Test
  void findStudentByIdNonExistentId_throwsException() {
    long id = 1L;
    when(studentRepository.findById(id)).thenReturn(Optional.empty());
    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> studentService.findStudentById(id));
    assertEquals("404 NOT_FOUND \"Student with id: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  void findStudentByIdWithNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> studentService.findStudentById(null));
    assertEquals("Please enter an id.", e.getMessage());
  }

  @Test
  void addStudentWhenEmailExists_throwsException() {
    String email = students.get(0).getEmail();
    when(studentRepository.existsStudentByEmail(email)).thenReturn(true);

    createStudentDto.setEmail("test@email.hu");

    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> studentService.addStudent(createStudentDto));

    assertTrue(studentRepository.existsStudentByEmail(email));
    assertEquals(
        "Email address is already registered. Student email address must be unique.",
        e.getMessage());
  }

  @Test
  public void addStudentWithNullObject_throwsException() {
    createStudentDto = null;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> studentService.addStudent(createStudentDto));
    assertEquals("The request body is empty, you must send a valid request.", e.getMessage());
  }

  @Test
  public void addStudentWithId_throwsException() {
    createStudentDto.setId(1L);
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> studentService.addStudent(createStudentDto));
    assertEquals("Student id must be null.", e.getMessage());
  }

  @Test
  public void addStudent_WorksCorrectly() {
    Student student = createStudentDto.convertToStudent();
    when(studentRepository.save(any(Student.class)))
        .thenReturn(student);
    StudentDto actual = studentService.addStudent(createStudentDto);

    StudentDto expected = StudentDto.convertToStudentDto(student);
    assertEquals(expected, actual);
  }

  @Test
  public void deleteStudentByIdNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> studentService.removeStudent(null));
    assertEquals("Please enter an id.", e.getMessage());
  }

  @Test
  public void deleteStudentByIdInvalidId_throwsException() {
    long id = 0L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> studentService.removeStudent(id));
    assertEquals("Invalid Student Id: " + id, e.getMessage());
  }

  @Test
  public void deleteStudentNonExistentRecord_throwsException() {
    Long id = 1L;
    when(studentRepository.findById(id)).thenReturn(Optional.empty());

    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> studentService.removeStudent(id));

    assertEquals("404 NOT_FOUND \"Student with id: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  void removeStudent_WorksCorrectly() {
    int id = 1;
    when(studentRepository.findById((long) id))
        .thenReturn(Optional.of(students.get(0)));
    StudentDto expected = StudentDto.convertToStudentDto(students.get(0));

    StudentDto actual = studentService.removeStudent((long) id);

    assertEquals(expected, actual);
  }

  @Test
  public void modifyStudentNullBody_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> studentService.modifyStudent(null, 1L));
    assertEquals("The request body is empty, you must send a valid request.", e.getMessage());
  }

  @Test
  public void modifyStudentNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> studentService.modifyStudent(updateStudentDto, null));
    assertEquals("Id of original student is null. Please enter an id.", e.getMessage());
  }

  @Test
  public void modifyStudentInvalidId_throwsException() {
    long id = -1L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> studentService.modifyStudent(updateStudentDto, id));
    assertEquals("Invalid Student Id: " + id, e.getMessage());
  }

  @Test
  public void modifyStudentNonExistentId_throwsException() {
    Long id = 1L;
    when(studentRepository.findById(id)).thenReturn(Optional.empty());

    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> studentService.modifyStudent(updateStudentDto, id));

    assertEquals("404 NOT_FOUND \"Id of original student: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  public void modifyStudentExistentEmail_throwsException() {
    int id = 2;
    when(studentRepository.findById(any())).thenReturn(Optional.of(students.get(0)));
    String email = students.get(0).getEmail();
    updateStudentDto.setEmail(email);
    when(studentRepository.findById((long) id)).thenReturn(Optional.of(students.get(id - 1)));
    when(studentRepository.existsStudentByEmail(email)).thenReturn(true);

    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> studentService.modifyStudent(updateStudentDto, (long) id));

    assertEquals(
        "Email address is already registered. Student email address must be unique.",
        e.getMessage());
  }

  @Test
  void modifyStudent_WorksCorrectly() {
    int id = 1;

    when(studentRepository.findById((long) id))
        .thenReturn(Optional.of(students.get(0)));

    StudentDto actual = studentService.modifyStudent(updateStudentDto, (long) id);

    StudentDto expected = StudentDto.convertToStudentDto(students.get(0));
    assertEquals(expected, actual);
  }

  @Test
  public void getCoursesOfStudentWithoutStatus_WorksCorrectly() {
    int studentId = 2;
    Student student = students.get(studentId - 1);
    Course course = courses.get(0);
    Set<Course> coursesOfStudent = student.getCourses();
    coursesOfStudent.add(course);
    when(studentRepository.findById((long) studentId)).thenReturn(Optional.of(student));

    Set<CourseForParticipantDto> expected = coursesOfStudent.stream()
        .map(CourseForParticipantDto::convertToCourseForParticipantDto)
        .collect(Collectors.toSet());
    CoursesDto actual = studentService.getCoursesOfStudent((long) studentId, null);
    assertEquals(new CoursesDto(expected), actual);
  }

  @Test
  public void getCoursesOfStudentWithStatus_WorksCorrectly() {
    int studentId = 2;
    String status = "planned";
    Student student = students.get(studentId - 1);
    Course course1 = courses.get(0);
    Course course2 = courses.get(1);
    Set<Course> coursesOfStudent = student.getCourses();
    coursesOfStudent.addAll(Arrays.asList(course1,course2));
    when(studentRepository.findById((long) studentId)).thenReturn(Optional.of(student));
    Set<CourseForParticipantDto> expected = coursesOfStudent.stream()
        .filter(course -> course.getStatus().equals(Status.valueOf(status.toUpperCase())))
        .map(CourseForParticipantDto::convertToCourseForParticipantDto)
        .collect(Collectors.toSet());
    CoursesDto actual = studentService.getCoursesOfStudent((long) studentId, status);
    assertEquals(new CoursesDto(expected), actual);
  }
}
