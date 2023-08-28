package com.greenfoxacademy.vocseikatimasterwork.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.EntityNotFoundException;
import com.greenfoxacademy.vocseikatimasterwork.models.CourseType;
import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CreateCourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.UpdateCourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentForCourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentsDto;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.ClassRoom;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Instructor;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Student;
import com.greenfoxacademy.vocseikatimasterwork.repositories.ClassRoomRepository;
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
public class DefaultCourseServiceTest {

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private ClassRoomRepository roomRepository;

  @Mock
  private InstructorRepository instructorRepository;

  @InjectMocks
  private DefaultCourseService courseService;

  private List<Course> courses;

  private CreateCourseDto createCourseDto;
  private UpdateCourseDto updateCourseDto;

  private final List<Instructor> instructors = Arrays.asList(
      new Instructor(1L, "Test", "Test name", Gender.FEMALE,
          LocalDate.of(2000, 1, 1), "test address",
          "test address", "test@email.hu", "test phone number",
          LocalDateTime.of(2021, 1, 1, 0, 0),
          LocalDateTime.of(2021, 1, 1, 0, 0)),
      new Instructor(2L, "Test2", "Test2 name", Gender.FEMALE,
          LocalDate.of(2000, 1, 1), "test2 address",
          "test2 address", "test2@email.hu", "test2 phone number",
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

  @BeforeEach
  void setUp() {

    courses = Arrays.asList(
        new Course(1L, "Test title",
            LocalDate.now().minusMonths(1),
            LocalDate.now().minusDays(10), 40, false,
            100000, CourseType.THEORETICAL, Status.FINISHED, instructors.get(0), rooms.get(0),
            LocalDateTime.of(2021, 1, 1, 0, 0),
            LocalDateTime.of(2021, 1, 1, 0, 0)),
        new Course(2L, "Test2 title",
            LocalDate.now().plusDays(10),
            LocalDate.now().plusDays(20), 40, false,
            100000, CourseType.PRACTICAL, Status.PLANNED, instructors.get(1), rooms.get(1),
            LocalDateTime.of(2021, 1, 1, 0, 0),
            LocalDateTime.of(2021, 1, 1, 0, 0)),
        new Course(3L, "Test3 title",
            LocalDate.now().minusDays(10),
            LocalDate.now().plusDays(10), 40, true,
            100000, CourseType.PRACTICAL, Status.IN_PROGRESS, instructors.get(0), rooms.get(0),
            LocalDateTime.of(2021, 1, 1, 0, 0),
            LocalDateTime.of(2021, 1, 1, 0, 0))
    );

    createCourseDto = new CreateCourseDto();
    createCourseDto.setTitle("post title");
    createCourseDto.setStartDate(LocalDate.of(2021, 8, 8));
    createCourseDto.setEndDate(LocalDate.of(2021, 8, 8));
    createCourseDto.setDurationInHours(8);
    createCourseDto.setIndividualEducation(true);
    createCourseDto.setPrice(50000.0);
    createCourseDto.setType(CourseType.THEORETICAL);
    createCourseDto.setClassRoomId(1L);

    updateCourseDto = new UpdateCourseDto();
    updateCourseDto.setId(10L);
    updateCourseDto.setTitle("put title");
    updateCourseDto.setStartDate(LocalDate.of(2021, 8, 10));
    updateCourseDto.setEndDate(LocalDate.of(2021, 8, 10));
    updateCourseDto.setClassRoomId(2L);
  }

  @Test
  public void listAllCoursesWithoutStatus_thenReturnsCorrectListOfDTOs() {
    when(courseRepository.findAll()).thenReturn(courses);

    List<CourseDto> actual = courses.stream()
        .map(CourseDto::convertToCourseDto).collect(Collectors.toList());

    List<CourseDto> result = courseService.listAllCourses(null);

    assertEquals(3, result.size());
    assertEquals(result, actual);
  }

  @Test
  public void listAllCoursesWithCorrectStatus_thenReturnsCorrectListOfDTOs() {
    String status = "finished";

    List<Course> coursesByStatus = courses.stream()
        .filter(course -> course.getStatus().equals(Status.valueOf(status.toUpperCase())))
        .collect(Collectors.toList());
    List<CourseDto> excepted =
        coursesByStatus.stream().map(CourseDto::convertToCourseDto).collect(Collectors.toList());

    when(courseRepository.findAllByStatus(Status.valueOf(status.toUpperCase())))
        .thenReturn(coursesByStatus);

    List<CourseDto> result = courseService.listAllCourses(status);

    assertEquals(1, result.size());
    assertEquals(excepted, result);
  }

  @Test
  public void listAllCoursesWithInvalidStatus_throwsException() {
    String status = "spongeBob";

    IllegalArgumentException e =
        assertThrows(IllegalArgumentException.class, () -> courseService.listAllCourses(status));

    assertEquals("Status must be from: " + Arrays.asList(Status.values()), e.getMessage());
  }

  @Test
  public void findCourseById_returnsDtoCorrectly() {
    when(courseRepository.findById(1L)).thenReturn(Optional.of(courses.get(0)));
    CourseDto actual = courseService.findCourseById(1L);
    CourseDto expected = CourseDto.convertToCourseDto(courses.get(0));
    assertEquals(expected, actual);
  }

  @Test
  void findCourseByIdNonExistentId_throwsException() {
    long id = 1L;
    when(courseRepository.findById(id)).thenReturn(Optional.empty());
    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> courseService.findCourseById(id));
    assertEquals("404 NOT_FOUND \"Course with id: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  void findCourseByIdWithNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseService.findCourseById(null));
    assertEquals("Please enter an id.", e.getMessage());
  }

  @Test
  void findCourseByIdWithInvalidId_throwsException() {
    long id = 0L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseService.findCourseById(id));
    assertEquals("Invalid Course Id: " + id, e.getMessage());
  }

  @Test
  public void addCourseWithNullObject_throwsException() {
    createCourseDto = null;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseService.addCourse(createCourseDto));
    assertEquals("The request body is empty, you must send a valid request.", e.getMessage());
  }

  @Test
  public void addCourseWithId_throwsException() {
    createCourseDto.setId(1L);
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseService.addCourse(createCourseDto));
    assertEquals("Course id must be null.", e.getMessage());
  }

  @Test
  public void addCourse_WorksCorrectly() {
    int roomId = 2;

    createCourseDto.setClassRoomId((long) roomId);
    when(roomRepository.findById(createCourseDto.getClassRoomId())).thenReturn(
        Optional.of(rooms.get(roomId - 1)));

    when(courseRepository
        .notAvailableRooms(createCourseDto.getStartDate(), createCourseDto.getEndDate()))
        .thenReturn(rooms.subList(0, 1));

    Course courseToAdd = createCourseDto.convertToCourse(null, rooms.get(roomId - 1));
    when(courseRepository.save(any(Course.class))).thenReturn(courseToAdd);

    CourseDto actual = courseService.addCourse(createCourseDto);
    assertEquals(CourseDto.convertToCourseDto(courseToAdd), actual);
  }

  @Test
  public void addCourseWithNonExistentRoomId_throwException() {
    when(roomRepository.findById(any())).thenReturn(Optional.empty());
    EntityNotFoundException e =
        assertThrows(EntityNotFoundException.class, () -> courseService.addCourse(createCourseDto));
    assertEquals(
        "404 NOT_FOUND \"Entity not found, not able to add course without existing classroom\"",
        e.getMessage());
  }

  @Test
  public void addCourseWithInvalidCourseDates_throwException() {
    when(roomRepository.findById(any())).thenReturn(Optional.of(rooms.get(0)));
    createCourseDto.setEndDate(createCourseDto.getStartDate().minusDays(1));
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseService.addCourse(createCourseDto));
    assertEquals("The start date cannot be after the end date.", e.getMessage());
  }

  @Test
  public void addCourseWithNotAvailableRoom_throwException() {
    when(roomRepository.findById(createCourseDto.getClassRoomId())).thenReturn(
        Optional.of(rooms.get(0)));

    when(courseRepository
        .notAvailableRooms(createCourseDto.getStartDate(), createCourseDto.getEndDate()))
        .thenReturn(rooms.subList(0, 1));

    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseService.addCourse(createCourseDto));
    assertEquals("This room is not available with this start and end date.", e.getMessage());
  }

  @Test
  public void addCourseWithExistentInstructorId_WorksCorrectly() {
    int roomId = 2;
    int instructorId = 2;
    createCourseDto.setInstructorId((long) instructorId);

    createCourseDto.setClassRoomId((long) roomId);
    when(roomRepository.findById(createCourseDto.getClassRoomId())).thenReturn(
        Optional.of(rooms.get(roomId - 1)));

    when(courseRepository
        .notAvailableRooms(createCourseDto.getStartDate(), createCourseDto.getEndDate()))
        .thenReturn(rooms.subList(0, 1));

    Course courseToAdd = createCourseDto.convertToCourse(null, rooms.get(roomId - 1));
    when(courseRepository.save(any(Course.class))).thenReturn(courseToAdd);

    when(instructorRepository.findById(createCourseDto.getInstructorId()))
        .thenReturn(Optional.of(instructors.get(instructorId - 1)));

    CourseDto actual = courseService.addCourse(createCourseDto);
    assertEquals(CourseDto.convertToCourseDto(courseToAdd), actual);
    assertEquals(CourseDto.convertToCourseDto(courseToAdd).getInstructorId(),
        actual.getInstructorId());
  }

  @Test
  public void addCourseWithNonExistentInstructorId_throwException() {
    int roomId = 2;
    int instructorId = 1;
    createCourseDto.setInstructorId((long) instructorId);

    createCourseDto.setClassRoomId((long) roomId);
    when(roomRepository.findById(createCourseDto.getClassRoomId())).thenReturn(
        Optional.of(rooms.get(roomId - 1)));

    when(courseRepository
        .notAvailableRooms(createCourseDto.getStartDate(), createCourseDto.getEndDate()))
        .thenReturn(rooms.subList(0, 1));

    when(instructorRepository.findById(createCourseDto.getInstructorId()))
        .thenReturn(Optional.empty());

    EntityNotFoundException e =
        assertThrows(EntityNotFoundException.class, () -> courseService.addCourse(createCourseDto));
    assertEquals("404 NOT_FOUND \"Instructor with id: " + instructorId + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  public void deleteCourseByIdNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseService.removeCourse(null));
    assertEquals("Please enter an id.", e.getMessage());
  }

  @Test
  public void deleteCourseByIdInvalidId_throwsException() {
    long id = 0L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseService.removeCourse(id));
    assertEquals("Invalid Course Id: " + id, e.getMessage());
  }

  @Test
  public void deleteCourseNonExistentRecord_throwsException() {
    Long id = 1L;
    when(courseRepository.findById(id)).thenReturn(Optional.empty());

    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> courseService.removeCourse(id));

    assertEquals("404 NOT_FOUND \"Course with id: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  void removeCourse_WorksCorrectly() {
    int id = 1;
    when(courseRepository.findById((long) id)).thenReturn(Optional.of(courses.get(0)));
    CourseDto expected = CourseDto.convertToCourseDto(courses.get(0));

    CourseDto actual = courseService.removeCourse((long) id);

    assertEquals(expected, actual);
  }

  @Test
  public void modifyCourseNullBody_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseService.modifyCourse(null, 1L));
    assertEquals("The request body is empty, you must send a valid request.", e.getMessage());
  }

  @Test
  public void modifyCourseNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseService.modifyCourse(updateCourseDto, null));
    assertEquals("Id of original course is null. Please enter an id.", e.getMessage());
  }

  @Test
  public void modifyCourseInvalidId_throwsException() {
    long id = -1L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseService.modifyCourse(updateCourseDto, id));
    assertEquals("Invalid Course Id: " + id, e.getMessage());
  }

  @Test
  public void modifyCourseNonExistentId_throwsException() {
    Long id = 1L;
    when(courseRepository.findById(id)).thenReturn(Optional.empty());

    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> courseService.modifyCourse(updateCourseDto, id));

    assertEquals("404 NOT_FOUND \"Id of original course: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  public void modifyCourseNonExistentRoomId_throwsException() {
    int courseId = 1;
    when(courseRepository.findById((long) courseId)).thenReturn(Optional.of(courses.get(0)));
    when(roomRepository.findById(any())).thenReturn(Optional.empty());
    EntityNotFoundException e =
        assertThrows(EntityNotFoundException.class,
            () -> courseService.modifyCourse(updateCourseDto, (long) courseId));
    assertEquals("404 NOT_FOUND \"Entity not found, not able to modify course " +
        "without existing classroom\"", e.getMessage());
  }

  @Test
  public void modifyCourseWithInvalidCourseDates_throwException() {
    int courseId = 1;
    when(courseRepository.findById((long) courseId)).thenReturn(Optional.of(courses.get(0)));
    when(roomRepository.findById(any())).thenReturn(Optional.of(rooms.get(1)));
    updateCourseDto.setEndDate(updateCourseDto.getStartDate().minusDays(1));
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseService.modifyCourse(updateCourseDto, (long) courseId));
    assertEquals("The start date cannot be after the end date.", e.getMessage());
  }

  @Test
  public void modifyCourseWithNotAvailableRoom_throwException() {
    int courseId = 3;
    int roomId = 2;
    when(courseRepository.findById((long) courseId))
        .thenReturn(Optional.of(courses.get(courseId - 1)));

    when(roomRepository.findById(any())).thenReturn(
        Optional.of(rooms.get(roomId - 1)));

    when(courseRepository
        .notAvailableRooms(updateCourseDto.getStartDate(), updateCourseDto.getEndDate()))
        .thenReturn(rooms.subList(1, 2));

    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> courseService.modifyCourse(updateCourseDto, (long) courseId));
    assertEquals("This room is not available with this start and end date.", e.getMessage());
  }

  @Test
  public void modifyCourseWithNonExistentInstructor_throwException() {
    int courseId = 1;
    when(courseRepository.findById(any())).thenReturn(Optional.of(courses.get(courseId - 1)));
    when(roomRepository.findById(any())).thenReturn(Optional.of(rooms.get(1)));
    long instructorId = 1L;
    updateCourseDto.setInstructorId(instructorId);
    when(instructorRepository.findById(updateCourseDto.getInstructorId()))
        .thenReturn(Optional.empty());
    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> courseService.modifyCourse(updateCourseDto, (long) courseId));
    assertEquals("404 NOT_FOUND \"Instructor with id: " + instructorId + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  public void modifyCourse_WorksCorrectly() {
    int courseId = 2;
    int roomId = 1;
    int instructorId = 1;
    Course originalCourse = courses.get(courseId - 1);

    when(courseRepository.findById((long) courseId)).thenReturn(Optional.of(originalCourse));

    when(roomRepository.findById(any())).thenReturn(Optional.of(rooms.get(roomId - 1)));

    when(courseRepository
        .notAvailableRooms(updateCourseDto.getStartDate(), updateCourseDto.getEndDate()))
        .thenReturn(rooms.subList(1, 2));

    updateCourseDto.setInstructorId((long) instructorId);
    when(instructorRepository.findById(updateCourseDto.getInstructorId()))
        .thenReturn(Optional.of(instructors.get(instructorId)));

    when(courseRepository.save(any(Course.class))).thenReturn(originalCourse);

    CourseDto result = courseService.modifyCourse(updateCourseDto, (long) courseId);

    assertEquals(CourseDto.convertToCourseDto(originalCourse), result);
  }

  @Test
  public void listStudentsOfCourseByCourseId_WorksCorrectly() {
    int courseId = 2;
    Course course = courses.get(courseId - 1);
    when(courseRepository.findById((long) courseId)).thenReturn(Optional.of(course));
    Set<Student> students = course.getStudents();
    students.add(this.students.get(0));
    students.add(this.students.get(1));
    Set<StudentForCourseDto> expected = students.stream()
        .map(StudentForCourseDto::convertToStudentForCourseDto)
        .collect(Collectors.toSet());
    StudentsDto actual = courseService.getStudentsOfCourse((long) courseId);
    assertEquals(new StudentsDto(expected), actual);
  }

}
