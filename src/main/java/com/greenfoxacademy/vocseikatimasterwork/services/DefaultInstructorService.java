package com.greenfoxacademy.vocseikatimasterwork.services;

import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNonNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.validateLongId;

import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.EntityNotFoundException;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CourseForParticipantDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CoursesDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.CreateInstructorDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.InstructorDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.UpdateInstructorDto;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Instructor;
import com.greenfoxacademy.vocseikatimasterwork.repositories.CourseRepository;
import com.greenfoxacademy.vocseikatimasterwork.repositories.InstructorRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DefaultInstructorService implements InstructorService {

  private final InstructorRepository instructorRepository;
  private final CourseRepository courseRepository;

  @Autowired
  public DefaultInstructorService(InstructorRepository instructorRepository,
                                  CourseRepository courseRepository) {
    this.instructorRepository = instructorRepository;
    this.courseRepository = courseRepository;
  }

  @Override
  public InstructorDto findInstructorById(Long id) {
    requireNonNull(id, "Please enter an id.");
    validateLongId(id, "Invalid Instructor Id: " + id);

    Instructor instructor = instructorRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Instructor with id: " + id + " cannot be found."));

    return InstructorDto.convertToInstructorDto(instructor);
  }

  @Override
  public List<InstructorDto> listAllInstructor() {
    return instructorRepository.findAll().stream()
        .map(InstructorDto::convertToInstructorDto)
        .collect(Collectors.toList());
  }

  @Override
  public InstructorDto addInstructor(CreateInstructorDto instructor) {
    requireNonNull(instructor, "The request body is empty, you must send a valid request.");
    requireNull(instructor.getId(), "Instructor id must be null.");

    if (instructorRepository.existsInstructorByEmail(instructor.getEmail())) {
      throw new IllegalArgumentException(
          "Email address is already registered. Instructor email address must be unique.");
    }

    Instructor savedInstructor = instructorRepository.save(instructor.convertToInstructor());
    return InstructorDto.convertToInstructorDto(savedInstructor);
  }

  @Override
  public InstructorDto removeInstructor(Long id) {
    requireNonNull(id, "Please enter an id.");
    validateLongId(id, "Invalid Instructor Id: " + id);

    Instructor instructor = instructorRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Instructor with id: " + id + " cannot be found."));

    instructorRepository.delete(instructor);
    return InstructorDto.convertToInstructorDto(instructor);
  }

  @Override
  public InstructorDto modifyInstructor(UpdateInstructorDto instructor,
                                        Long idOfOriginalInstructor) {
    requireNonNull(instructor, "The request body is empty, you must send a valid request.");
    requireNonNull(idOfOriginalInstructor,
        "Id of original instructor is null. Please enter an id.");
    validateLongId(idOfOriginalInstructor, "Invalid Instructor ID: " + idOfOriginalInstructor);

    Instructor originalInstructor =
        instructorRepository.findById(idOfOriginalInstructor).orElseThrow(
            () -> new EntityNotFoundException(
                "Instructor with id: " + idOfOriginalInstructor + " cannot be found."));

    if (instructor.getFirstName() != null && !instructor.getFirstName().isEmpty()) {
      originalInstructor.setFirstName(instructor.getFirstName());
    }
    if (instructor.getLastName() != null && !instructor.getLastName().isEmpty()) {
      originalInstructor.setLastName(instructor.getLastName());
    }
    if (instructor.getGender() != null) {
      originalInstructor.setGender(instructor.getGender());
    }
    if (instructor.getDateOfBirth() != null) {
      originalInstructor.setDateOfBirth(instructor.getDateOfBirth());
    }
    if (instructor.getMailingAddress() != null && !instructor.getMailingAddress().isEmpty()) {
      originalInstructor.setMailingAddress(instructor.getMailingAddress());
    }
    if (instructor.getBillingAddress() != null && !instructor.getBillingAddress().isEmpty()) {
      originalInstructor.setBillingAddress(instructor.getBillingAddress());
    }
    if (instructor.getEmail() != null && !instructor.getEmail().isEmpty()) {
      if (!originalInstructor.getEmail().equals(instructor.getEmail())
          && instructorRepository.existsInstructorByEmail(instructor.getEmail())) {
        throw new IllegalArgumentException(
            "Email address is already registered. Instructor email address must be unique.");
      }
      originalInstructor.setEmail(instructor.getEmail());
    }
    if (instructor.getPhoneNumber() != null && !instructor.getPhoneNumber().isEmpty()) {
      originalInstructor.setPhoneNumber(instructor.getPhoneNumber());
    }

    instructorRepository.save(originalInstructor);
    return InstructorDto.convertToInstructorDto(originalInstructor);
  }

  @Override
  public CoursesDto getCoursesOfInstructor(Long instructorId, String inputStatus) {
    requireNonNull(instructorId, "Please enter an instructor id.");
    validateLongId(instructorId, "Invalid Instructor Id: " + instructorId);

    Instructor instructor =
        instructorRepository.findById(instructorId).orElseThrow(() -> new EntityNotFoundException(
            "Instructor with id: " + instructorId + " cannot be found."));


    if (inputStatus == null || inputStatus.isEmpty()) {
      Set<Course> courses = courseRepository.findAllByInstructorId(instructor.getId());
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
    Set<Course> coursesByInstructorIdAndStatus = courseRepository
        .findAllByInstructorIdAndStatus(instructorId, Status.valueOf(inputStatus.toUpperCase()));

    Set<CourseForParticipantDto> courseDtoSetByStatus = coursesByInstructorIdAndStatus.stream()
        .map(CourseForParticipantDto::convertToCourseForParticipantDto)
        .collect(Collectors.toSet());
    return new CoursesDto(courseDtoSetByStatus);
  }
}
