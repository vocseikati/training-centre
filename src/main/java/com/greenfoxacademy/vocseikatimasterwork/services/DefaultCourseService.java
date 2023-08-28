package com.greenfoxacademy.vocseikatimasterwork.services;

import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNonNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.validateDates;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.validateLongId;

import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.EntityNotFoundException;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultCourseService implements CourseService {

  private final CourseRepository courseRepository;
  private final InstructorRepository instructorRepository;
  private final ClassRoomRepository roomRepository;

  @Autowired
  public DefaultCourseService(CourseRepository courseRepository,
                              InstructorRepository instructorRepository,
                              ClassRoomRepository roomRepository) {
    this.courseRepository = courseRepository;
    this.instructorRepository = instructorRepository;
    this.roomRepository = roomRepository;
  }

  @Override
  @Transactional
  public CourseDto findCourseById(Long id) {
    requireNonNull(id, "Please enter an id.");
    validateLongId(id, "Invalid Course Id: " + id);

    Course course = courseRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Course with id: " + id + " cannot be found."));

    return CourseDto.convertToCourseDto(course);
  }

  @Override
  @Transactional
  public List<CourseDto> listAllCourses(String status) {
    if (status == null || status.isEmpty()) {
      return courseRepository.findAll().stream()
          .map(CourseDto::convertToCourseDto)
          .collect(Collectors.toList());
    }
    try {
      Status.valueOf(status.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Status must be from: " + Arrays.asList(Status.values()));
    }

    List<Course> coursesByStatus =
        courseRepository.findAllByStatus(Status.valueOf(status.toUpperCase()));

    return coursesByStatus.stream()
        .map(CourseDto::convertToCourseDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public CourseDto addCourse(CreateCourseDto course) {
    requireNonNull(course, "The request body is empty, you must send a valid request.");
    requireNull(course.getId(), "Course id must be null.");

    ClassRoom classRoom = roomRepository.findById(course.getClassRoomId()).orElseThrow(
        () -> new EntityNotFoundException(
            "Entity not found, not able to add course without existing classroom"));

    validateDates(course.getStartDate(), course.getEndDate(),
        "The start date cannot be after the end date.");

    checkAvailableRooms(course.getStartDate(), course.getEndDate(), classRoom);

    Long instructorId = course.getInstructorId();

    Instructor instructor = null;

    if (instructorId != null) {
      instructor = instructorRepository.findById(instructorId).orElseThrow(
          () -> new EntityNotFoundException(
              "Instructor with id: " + instructorId + " cannot be found."));
    }
    Course courseSaved =
        courseRepository.save(course.convertToCourse(instructor, classRoom));
    return CourseDto.convertToCourseDto(courseSaved);
  }

  @Override
  @Transactional
  public CourseDto removeCourse(Long id) {
    requireNonNull(id, "Please enter an id.");
    validateLongId(id, "Invalid Course Id: " + id);

    Course course = courseRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Course with id: " + id + " cannot be found."));

    courseRepository.deleteById(id);
    return CourseDto.convertToCourseDto(course);
  }

  @Override
  @Transactional
  public CourseDto modifyCourse(UpdateCourseDto course, Long idOfOriginalCourse) {
    requireNonNull(course, "The request body is empty, you must send a valid request.");
    requireNonNull(idOfOriginalCourse, "Id of original course is null. Please enter an id.");
    validateLongId(idOfOriginalCourse, "Invalid Course Id: " + idOfOriginalCourse);

    Course originalCourse = courseRepository.findById(idOfOriginalCourse)
        .orElseThrow(() -> new EntityNotFoundException(
            "Id of original course: " + idOfOriginalCourse + " cannot be found."));

    ClassRoom classRoom = roomRepository.findById(course.getClassRoomId()).orElseThrow(
        () -> new EntityNotFoundException(
            "Entity not found, not able to modify course without existing classroom"));

    validateDates(course.getStartDate(), course.getEndDate(),
        "The start date cannot be after the end date.");

    checkAvailableRooms(course.getStartDate(), course.getEndDate(), classRoom);

    originalCourse.setStartDate(course.getStartDate());
    originalCourse.setEndDate(course.getEndDate());

    if (course.getTitle() != null && !course.getTitle().isEmpty()) {
      originalCourse.setTitle(course.getTitle());
    }
    if (course.getDurationInHours() != null) {
      originalCourse.setDurationInHours(course.getDurationInHours());
    }
    if (course.getIndividualEducation() != null) {
      originalCourse.setIndividualEducation(course.getIndividualEducation());
    }
    if (course.getPrice() != null) {
      originalCourse.setPrice(course.getPrice());
    }
    if (course.getType() != null) {
      originalCourse.setType(course.getType());
    }
    if (course.getInstructorId() != null) {
      Long instructorId = course.getInstructorId();
      Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(
          () -> new EntityNotFoundException(
              "Instructor with id: " + instructorId + " cannot be found."));
      originalCourse.setInstructor(instructor);
    }

    courseRepository.save(originalCourse);
    return CourseDto.convertToCourseDto(originalCourse);
  }

  @Override
  public StudentsDto getStudentsOfCourse(Long courseId) {
    requireNonNull(courseId, "Please enter a course id.");
    validateLongId(courseId, "Invalid Course Id: " + courseId);

    Course course = courseRepository.findById(courseId).orElseThrow(
        () -> new EntityNotFoundException("Course with id: " + courseId + " cannot be found."));

    Set<Student> students = course.getStudents();

    Set<StudentForCourseDto> studentDtoSet = students.stream()
        .map(StudentForCourseDto::convertToStudentForCourseDto)
        .collect(Collectors.toSet());

    return new StudentsDto(studentDtoSet);
  }

  private void checkAvailableRooms(LocalDate startDate, LocalDate endDate, ClassRoom classRoom) {
    List<ClassRoom> notAvailableRooms = courseRepository.notAvailableRooms(startDate, endDate);
    if (notAvailableRooms.contains(classRoom)) {
      throw new IllegalArgumentException(
          "This room is not available with this start and end date.");
    }
  }

}
