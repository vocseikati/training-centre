package com.greenfoxacademy.vocseikatimasterwork.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.greenfoxacademy.vocseikatimasterwork.models.CourseType;
import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.ClassRoom;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Instructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext
public class CourseRepositoryTest {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private InstructorRepository instructorRepository;

  @Autowired
  private ClassRoomRepository roomRepository;

  private final List<Instructor> instructors = Arrays.asList(
      new Instructor(1L, "Varga", "Csilla", Gender.FEMALE, LocalDate.of(1973, 5, 21),
          "Fogócska utca 1., 1113 Budapest Hungary", "Fogócska utca 1., 1113 Budapest Hungary",
          "vcsilla@gmail.com", "0036302532801",
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0)),
      new Instructor(2L, "Alma", "Virág", Gender.FEMALE, LocalDate.of(1984, 3, 25),
          "Etele út 54/a., 1115 Budapest Hungary", "Etele út 54/a., 1115 Budapest Hungary",
          "almavirag@gmail.com", "0036306984210",
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0))
  );

  private final List<ClassRoom> rooms = Arrays.asList(
      new ClassRoom(1L, "theoretical room", "Lajos utca, Budapest 1033", 10,
          LocalDateTime.of(2021, 7, 24, 0, 0),
          LocalDateTime.of(2021, 7, 24, 0, 0)),
      new ClassRoom(2L, "practical room", "Bécsi út 96, Budapest 1035", 5,
          LocalDateTime.of(2021, 7, 20, 0, 0),
          LocalDateTime.of(2021, 7, 24, 0, 0))
  );

  private final List<Course> expected = Arrays.asList(
      new Course(1L, "exam preparation course",
          LocalDate.of(2021, 6, 1),
          LocalDate.of(2021, 6, 1), 8, true,
          40000, CourseType.THEORETICAL, Status.FINISHED, instructors.get(0), rooms.get(0),
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0)),
      new Course(2L, "women basic and fashion haircuts",
          LocalDate.of(2021, 6, 10),
          LocalDate.of(2021, 6, 11), 16, false,
          30000, CourseType.PRACTICAL, Status.FINISHED, instructors.get(1), rooms.get(1),
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0)),
      new Course(3L, "men fashion and classic haircut technologies",
          LocalDate.of(2021, 6, 1),
          LocalDate.of(2021, 6, 9), 40, false,
          32000, CourseType.PRACTICAL, Status.FINISHED, instructors.get(0), rooms.get(1),
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0)),
      new Course(4L, "haircut techniques",
          LocalDate.of(2021, 6, 20),
          LocalDate.of(2021, 6, 20), 8, true,
          40000, CourseType.THEORETICAL, Status.FINISHED, instructors.get(0), rooms.get(0),
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0)),
      new Course(5L, "hair drying for women",
          LocalDate.of(2021, 7, 21),
          LocalDate.of(2021, 7, 22), 15, false,
          30000, CourseType.PRACTICAL, Status.IN_PROGRESS, instructors.get(1), rooms.get(1),
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0)),
      new Course(6L, "painting techniques",
          LocalDate.of(2021, 8, 1),
          LocalDate.of(2021, 9, 15), 60, false,
          35000, CourseType.PRACTICAL, Status.IN_PROGRESS, instructors.get(1), rooms.get(1),
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0)),
      new Course(7L, "haircuts with razors and trimmers",
          LocalDate.of(2021, 9, 18),
          LocalDate.of(2021, 9, 18), 8, false,
          30000, CourseType.PRACTICAL, Status.PLANNED, instructors.get(0), rooms.get(0),
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0))
  );

  @Test
  public void whenFindAllCalled_ThenCorrectlyReturnsCourseList() {
    List<Course> allCourses = courseRepository.findAll();
    assertEquals(7, allCourses.size());
    assertEquals(expected, allCourses);
  }

  @Test
  public void whenFindByIsCalledCorrectly_ThenReturnsCorrectly() {
    Optional<Course> courseById = courseRepository.findById(1L);
    assertTrue(courseById.isPresent());
    assertEquals(expected.get(0), courseById.get());
  }

  @Test
  public void whenFindAllByInstructorIdCalled_ThenReturnsCorrectly() {
    Set<Course> allCourseByInstructorId =
        courseRepository.findAllByInstructorId(instructors.get(0).getId());
    assertEquals(4, allCourseByInstructorId.size());
    Set<Course> expectedCourseSetByInstructorId =
        new HashSet<>(Arrays.asList(expected.get(0), expected.get(2),
            expected.get(3), expected.get(6)));
    assertEquals(expectedCourseSetByInstructorId, allCourseByInstructorId);
  }

  @Test
  public void whenFindAllByStatusCalled_ThenReturnsCorrectly() {
    List<Course> allByStatus = courseRepository.findAllByStatus(Status.IN_PROGRESS);
    assertEquals(2, allByStatus.size());
    assertEquals(Arrays.asList(expected.get(4), expected.get(5)), allByStatus);
  }

  @Test
  public void whenFindAllByInstructorIdAndStatusCalled_ThenReturnsCorrectly() {
    Set<Course> allByInstructorIdAndStatus =
        courseRepository.findAllByInstructorIdAndStatus(instructors.get(0).getId(), Status.PLANNED);
    assertEquals(1, allByInstructorIdAndStatus.size());
    Set<Course> expected = new HashSet<>(Collections.singletonList(this.expected.get(6)));
    assertEquals(expected, allByInstructorIdAndStatus);
  }

  @Test
  public void whenDeleteAllByClassRoomCalled_ThenReturnsCorrectly() {
    courseRepository.deleteAllByClassRoom(rooms.get(0), Status.PLANNED);
    List<Course> courseListAfterDeletedRoom = courseRepository.findAll();

    List<Course> expectedList = expected.subList(0, 6);
    assertEquals(expectedList, courseListAfterDeletedRoom);
  }

  @Test
  public void whenNotAvailableRoomsCalled_ThenReturnsCorrectly() {
    List<ClassRoom> notAvailableRooms = courseRepository.notAvailableRooms(
        LocalDate.of(2021, 8, 1),
        LocalDate.of(2021, 8, 10));
    assertEquals(1, notAvailableRooms.size());
    assertEquals(Collections.singletonList(expected.get(5).getClassRoom()), notAvailableRooms);
  }
}
