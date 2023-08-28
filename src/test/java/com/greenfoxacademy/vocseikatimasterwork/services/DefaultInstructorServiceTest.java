package com.greenfoxacademy.vocseikatimasterwork.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.EntityNotFoundException;
import com.greenfoxacademy.vocseikatimasterwork.models.CourseType;
import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CourseForParticipantDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CoursesDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.CreateInstructorDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.InstructorDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.UpdateInstructorDto;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.ClassRoom;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Instructor;
import com.greenfoxacademy.vocseikatimasterwork.repositories.CourseRepository;
import com.greenfoxacademy.vocseikatimasterwork.repositories.InstructorRepository;
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
public class DefaultInstructorServiceTest {

  @Mock
  private InstructorRepository instructorRepository;

  @Mock
  private CourseRepository courseRepository;

  @InjectMocks
  private DefaultInstructorService instructorService;

  private List<Instructor> instructors;

  private CreateInstructorDto createInstructorDto;
  private UpdateInstructorDto updateInstructorDto;

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

    instructors = Arrays.asList(
        new Instructor(1L, "Test", "Test name", Gender.FEMALE,
            LocalDate.of(2000, 1, 1), "test address", "test address",
            "test@email.hu", "test phone number",
            LocalDateTime.of(2021, 1, 1, 0, 0),
            LocalDateTime.of(2021, 1, 1, 0, 0)),
        new Instructor(2L, "Test2", "Test2 name", Gender.FEMALE,
            LocalDate.of(2000, 1, 1), "test2 address", "test2 address",
            "test2@email.hu", "test2 phone number",
            LocalDateTime.of(2021, 1, 1, 0, 0),
            LocalDateTime.of(2021, 1, 1, 0, 0))
    );

    createInstructorDto = new CreateInstructorDto();
    createInstructorDto.setFirstName("create test");
    createInstructorDto.setLastName("post test");
    createInstructorDto.setGender(Gender.FEMALE);
    createInstructorDto.setDateOfBirth(LocalDate.of(2000, 1, 1));
    createInstructorDto.setMailingAddress("post address");
    createInstructorDto.setBillingAddress("post address");
    createInstructorDto.setEmail("post email");
    createInstructorDto.setPhoneNumber("post phone number");

    updateInstructorDto = new UpdateInstructorDto();
    updateInstructorDto.setId(1L);
    updateInstructorDto.setFirstName("put test");
    updateInstructorDto.setLastName("put test");
    updateInstructorDto.setGender(Gender.FEMALE);
    updateInstructorDto.setDateOfBirth(LocalDate.of(2000, 1, 1));
    updateInstructorDto.setMailingAddress("put test address");
    updateInstructorDto.setBillingAddress("put test address");
    updateInstructorDto.setEmail("put test email");
    updateInstructorDto.setPhoneNumber("put test phone number");
  }

  @Test
  public void listAllInstructor_thenReturnsCorrectListOfDTOs() {
    when(instructorRepository.findAll()).thenReturn(instructors);

    List<InstructorDto> actual = instructors.stream()
        .map(InstructorDto::convertToInstructorDto).collect(Collectors.toList());

    List<InstructorDto> result = instructorService.listAllInstructor();

    assertEquals(2, result.size());
    assertEquals(result, actual);
  }

  @Test
  public void findInstructorById_returnsDtoCorrectly() {
    when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructors.get(0)));
    InstructorDto actual = instructorService.findInstructorById(1L);
    InstructorDto expected = InstructorDto.convertToInstructorDto(instructors.get(0));
    assertEquals(expected, actual);
  }

  @Test
  void findInstructorByIdNonExistentId_throwsException() {
    long id = 1L;
    when(instructorRepository.findById(id)).thenReturn(Optional.empty());
    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> instructorService.findInstructorById(id));
    assertEquals("404 NOT_FOUND \"Instructor with id: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  void findInstructorByIdWithNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> instructorService.findInstructorById(null));
    assertEquals("Please enter an id.", e.getMessage());
  }

  @Test
  void addInstructorWhenEmailExists_throwsException() {
    String email = instructors.get(0).getEmail();
    when(instructorRepository.existsInstructorByEmail(email)).thenReturn(true);

    createInstructorDto.setEmail(email);

    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> instructorService.addInstructor(createInstructorDto));

    assertEquals(
        "Email address is already registered. Instructor email address must be unique.",
        e.getMessage());
  }

  @Test
  public void addInstructorWithNullObject_throwsException() {
    createInstructorDto = null;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> instructorService.addInstructor(createInstructorDto));
    assertEquals("The request body is empty, you must send a valid request.", e.getMessage());
  }

  @Test
  public void addInstructorWithId_throwsException() {
    createInstructorDto.setId(1L);
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> instructorService.addInstructor(createInstructorDto));
    assertEquals("Instructor id must be null.", e.getMessage());
  }

  @Test
  public void addInstructor_WorksCorrectly() {
    Instructor instructor = createInstructorDto.convertToInstructor();
    when(instructorRepository.save(any(Instructor.class)))
        .thenReturn(instructor);
    InstructorDto actual = instructorService.addInstructor(createInstructorDto);

    InstructorDto expected = InstructorDto.convertToInstructorDto(instructor);
    assertEquals(expected, actual);
  }

  @Test
  public void deleteInstructorByIdNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> instructorService.removeInstructor(null));
    assertEquals("Please enter an id.", e.getMessage());
  }

  @Test
  public void deleteInstructorByIdInvalidId_throwsException() {
    long id = 0L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> instructorService.removeInstructor(id));
    assertEquals("Invalid Instructor Id: " + id, e.getMessage());
  }

  @Test
  public void deleteInstructorNonExistentRecord_throwsException() {
    Long id = 1L;
    when(instructorRepository.findById(id)).thenReturn(Optional.empty());

    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> instructorService.removeInstructor(id));

    assertEquals("404 NOT_FOUND \"Instructor with id: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  void removeInstructor_WorksCorrectly() {
    int id = 1;
    when(instructorRepository.findById((long) id))
        .thenReturn(Optional.of(instructors.get(0)));
    InstructorDto expected = InstructorDto.convertToInstructorDto(instructors.get(0));

    InstructorDto actual = instructorService.removeInstructor((long) id);

    assertEquals(expected, actual);
  }

  @Test
  public void modifyInstructorNullBody_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> instructorService.modifyInstructor(null, 1L));
    assertEquals("The request body is empty, you must send a valid request.", e.getMessage());
  }

  @Test
  public void modifyInstructorNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> instructorService.modifyInstructor(updateInstructorDto, null));
    assertEquals("Id of original instructor is null. Please enter an id.", e.getMessage());
  }

  @Test
  public void modifyInstructorInvalidId_throwsException() {
    long id = -1L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> instructorService.modifyInstructor(updateInstructorDto, id));
    assertEquals("Invalid Instructor ID: " + id, e.getMessage());
  }

  @Test
  public void modifyInstructorNonExistentId_throwsException() {
    Long id = 1L;
    when(instructorRepository.findById(id)).thenReturn(Optional.empty());

    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> instructorService.modifyInstructor(updateInstructorDto, id));

    assertEquals("404 NOT_FOUND \"Instructor with id: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  public void modifyInstructorExistentEmail_throwsException() {
    int id = 2;
    when(instructorRepository.findById(any())).thenReturn(Optional.of(instructors.get(0)));
    String email = instructors.get(0).getEmail();
    updateInstructorDto.setEmail(email);
    when(instructorRepository.findById((long) id)).thenReturn(Optional.of(instructors.get(id - 1)));
    when(instructorRepository.existsInstructorByEmail(email)).thenReturn(true);

    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> instructorService.modifyInstructor(updateInstructorDto, (long) id));

    assertEquals(
        "Email address is already registered. Instructor email address must be unique.",
        e.getMessage());
  }

  @Test
  public void modifyInstructor_WorksCorrectly() {
    int id = 1;

    when(instructorRepository.findById((long) id)).thenReturn(Optional.of(instructors.get(0)));

    InstructorDto actual = instructorService.modifyInstructor(updateInstructorDto, (long) id);

    InstructorDto expected = InstructorDto.convertToInstructorDto(instructors.get(0));
    assertEquals(expected, actual);
  }

  @Test
  public void getCoursesOfInstructorWithoutStatus_WorksCorrectly() {
    int instructorId = 2;
    Instructor instructor = instructors.get(instructorId - 1);
    Course course = courses.get(0);
    course.setInstructor(instructor);
    Set<Course> coursesOfInstructor = instructor.getCourses();
    coursesOfInstructor.add(course);
    when(instructorRepository.findById((long) instructorId))
        .thenReturn(Optional.of(instructor));
    when(courseRepository.findAllByInstructorId((long) instructorId))
        .thenReturn(coursesOfInstructor);
    Set<CourseForParticipantDto> expected = coursesOfInstructor.stream()
        .map(CourseForParticipantDto::convertToCourseForParticipantDto)
        .collect(Collectors.toSet());
    CoursesDto actual = instructorService.getCoursesOfInstructor((long) instructorId, null);
    assertEquals(new CoursesDto(expected), actual);
  }

  @Test
  public void getCoursesOfInstructorWithStatus_WorksCorrectly() {
    int instructorId = 2;
    String status = "planned";
    Instructor instructor = instructors.get(instructorId - 1);
    Course course1 = courses.get(0);
    Course course2 = courses.get(1);
    course1.setInstructor(instructor);
    course2.setInstructor(instructor);
    Set<Course> coursesOfInstructor = instructor.getCourses();
    coursesOfInstructor.addAll(Arrays.asList(course1, course2));
    Set<Course> filteredCourses = coursesOfInstructor.stream()
        .filter(course -> course.getStatus().equals(Status.valueOf(status.toUpperCase())))
        .collect(Collectors.toSet());
    when(instructorRepository.findById((long) instructorId))
        .thenReturn(Optional.of(instructor));
    when(courseRepository.findAllByInstructorIdAndStatus((long) instructorId,
        Status.valueOf(status.toUpperCase()))).thenReturn(filteredCourses);
    Set<CourseForParticipantDto> expected = filteredCourses.stream()
        .map(CourseForParticipantDto::convertToCourseForParticipantDto)
        .collect(Collectors.toSet());
    CoursesDto actual =
        instructorService.getCoursesOfInstructor((long) instructorId, status);
    assertEquals(new CoursesDto(expected), actual);
  }
}
