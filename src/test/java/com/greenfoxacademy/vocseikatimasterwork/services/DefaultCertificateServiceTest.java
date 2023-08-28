package com.greenfoxacademy.vocseikatimasterwork.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.EntityNotFoundException;
import com.greenfoxacademy.vocseikatimasterwork.models.CourseType;
import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.certificatesdtos.CertificateDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.certificatesdtos.Certificates;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Certificate;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Student;
import com.greenfoxacademy.vocseikatimasterwork.repositories.CertificateRepository;
import com.greenfoxacademy.vocseikatimasterwork.repositories.StudentRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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
public class DefaultCertificateServiceTest {

  @Mock
  private CertificateRepository certificateRepository;

  @Mock
  private StudentRepository studentRepository;

  @InjectMocks
  private DefaultCertificateService certificateService;

  private List<Certificate> certificates;

  private CertificateDto certificateDto;

  private final List<Student> students = Arrays.asList(
      new Student(1L, "Test", "Test name", Gender.FEMALE,
          LocalDate.of(2000, 1, 1), "test address",
          "test address", "test@email.hu", "test phone number",
          "beginner", LocalDateTime.of(2021, 1, 1, 0, 0),
          LocalDateTime.of(2021, 1, 1, 0, 0)),
      new Student(2L, "Test2", "Test2 name", Gender.FEMALE,
          LocalDate.of(2000, 1, 1), "test2 address", "test2 address",
          "test2@email.hu", "test2 phone number", "test",
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
          100000, CourseType.PRACTICAL, Status.IN_PROGRESS, null, null,
          LocalDateTime.of(2021, 1, 1, 0, 0),
          LocalDateTime.of(2021, 1, 1, 0, 0))
  );

  @BeforeEach
  void setUp() {
    certificates = Arrays.asList(
        new Certificate(1L, "test student first name",
            "test student last name", LocalDate.of(2021, 8, 1),
            LocalDate.of(2021, 8, 2), 16,
            "test title", false, LocalDate.of(2021, 8, 30),
            students.get(0)),
        new Certificate(2L, "test2 student first name",
            "test2 student last name", LocalDate.of(2021, 7, 30),
            LocalDate.of(2021, 8, 10), 16,
            "test2 title", false, LocalDate.of(2021, 8, 30),
            students.get(1))
    );

    certificateDto = new CertificateDto();
    certificateDto.setCourseTitle("put title");
  }

  @Test
  public void listAllCertificateWithoutStudent_thenReturnsCorrectListOfDTOs() {
    when(certificateRepository.findAll()).thenReturn(certificates);

    List<CertificateDto> actual = certificates.stream()
        .map(CertificateDto::convertToCertificateDto).collect(Collectors.toList());

    List<CertificateDto> result = certificateService.getAllCertificates(null);

    assertEquals(2, result.size());
    assertEquals(actual, result);
  }

  @Test
  public void listAllCertificateWithExistentStudent_thenReturnsCorrectListOfDTOs() {
    List<Certificate> expected = Collections.singletonList(certificates.get(0));
    Long studentId = students.get(0).getId();
    when(certificateRepository.findAllByStudentId(studentId)).thenReturn(
        expected);
    when(studentRepository.existsById(studentId)).thenReturn(true);
    List<CertificateDto> expectedDTO =
        expected.stream().map(CertificateDto::convertToCertificateDto).collect(Collectors.toList());
    List<CertificateDto> actual = certificateService.getAllCertificates(studentId);
    assertEquals(expectedDTO, actual);
  }

  @Test
  public void listAllCertificateWithNonExistentStudent_throwsException() {
    Long studentId = students.get(0).getId();
    when(studentRepository.existsById(studentId)).thenReturn(false);
    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> certificateService.getAllCertificates(studentId));
    assertEquals("404 NOT_FOUND \"Student with id: " + studentId + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  public void findCertificateById_returnsDtoCorrectly() {
    int id = 1;
    when(certificateRepository.findById((long) id)).thenReturn(Optional.of(certificates.get(0)));
    CertificateDto actual = certificateService.getCertificateById((long) id);
    CertificateDto expected = CertificateDto.convertToCertificateDto(certificates.get(0));
    assertEquals(expected, actual);
  }

  @Test
  public void findCertificateByIdNonExistentId_throwsException() {
    long id = 1L;
    when(certificateRepository.findById(id)).thenReturn(Optional.empty());
    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> certificateService.getCertificateById(id));
    assertEquals("404 NOT_FOUND \"Certificate with id: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  public void findCertificateByIdWithNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> certificateService.getCertificateById(null));
    assertEquals("Please enter an id.", e.getMessage());
  }

  @Test
  public void findRoomByIdWithInvalidId_throwsException() {
    long id = 0L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> certificateService.getCertificateById(id));
    assertEquals("Invalid Certificate Id: " + id, e.getMessage());
  }

  @Test
  public void editCertificateWithNullCourse_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> certificateService.editCertificate(null));
    assertEquals("Course must not be null.", e.getMessage());
  }

  @Test
  public void editCertificateWithNotFinishedCourse_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> certificateService.editCertificate(courses.get(2)));
    assertEquals("The course is not over yet.", e.getMessage());
  }

  @Test
  public void editCertificate_worksCorrectly() {
    Course course = courses.get(0);
    course.setStudents(new HashSet<>(students));

    Certificate certificate1 = new Certificate();
    certificate1.setStudentFirstName(students.get(0).getFirstName());
    certificate1.setStudentLastName(students.get(0).getLastName());
    certificate1.setCourseStartDate(course.getStartDate());
    certificate1.setCourseEndDate(course.getEndDate());
    certificate1.setCourseDurationInHours(course.getDurationInHours());
    certificate1.setCourseTitle(course.getTitle());
    certificate1.setDone(true);
    certificate1.setStudent(students.get(0));

    Certificate certificate2 = new Certificate();
    certificate2.setStudentFirstName(students.get(1).getFirstName());
    certificate2.setStudentLastName(students.get(1).getLastName());
    certificate2.setCourseStartDate(course.getStartDate());
    certificate2.setCourseEndDate(course.getEndDate());
    certificate2.setCourseDurationInHours(course.getDurationInHours());
    certificate2.setCourseTitle(course.getTitle());
    certificate2.setDone(true);
    certificate2.setStudent(students.get(1));

    certificates = Arrays.asList(certificate1, certificate2);
    when(certificateRepository.saveAll(any())).thenReturn(certificates);
    Set<CertificateDto> expected =
        certificates.stream().map(CertificateDto::convertToCertificateDto)
            .collect(Collectors.toSet());

    Certificates result = certificateService.editCertificate(course);

    assertEquals(new Certificates(expected), result);
  }

  @Test
  public void deleteCertificateByIdNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> certificateService.deleteCertificate(null));
    assertEquals("Please enter an id.", e.getMessage());
  }

  @Test
  public void deleteCertificateByIdInvalidId_throwsException() {
    long id = 0L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> certificateService.deleteCertificate(id));
    assertEquals("Invalid Certificate Id: " + id, e.getMessage());
  }

  @Test
  public void deleteCertificateNonExistentRecord_throwsException() {
    Long id = 1L;
    when(certificateRepository.findById(id)).thenReturn(Optional.empty());

    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> certificateService.deleteCertificate(id));

    assertEquals("404 NOT_FOUND \"Certificate with id: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  void deleteCertificate_WorksCorrectly() {
    int id = 1;
    Certificate certificateToDelete = certificates.get(0);
    when(certificateRepository.findById((long) id)).thenReturn(Optional.of(certificateToDelete));
    CertificateDto expected = CertificateDto.convertToCertificateDto(certificateToDelete);

    CertificateDto actual = certificateService.deleteCertificate((long) id);

    assertEquals(expected, actual);
  }

  @Test
  public void modifyCertificateNullBody_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> certificateService.modifyCertificate(null, 1L));
    assertEquals("The request body is empty, you must send a valid request.", e.getMessage());
  }

  @Test
  public void modifyCertificateNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> certificateService.modifyCertificate(certificateDto, null));
    assertEquals("Id of original certificate is null. Please enter an id.", e.getMessage());
  }

  @Test
  public void modifyCertificateInvalidId_throwsException() {
    long id = -1L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> certificateService.modifyCertificate(certificateDto, id));
    assertEquals("Invalid Certificate Id: " + id, e.getMessage());
  }

  @Test
  public void modifyCertificateNonExistentId_throwsException() {
    Long id = 1L;
    when(certificateRepository.findById(id)).thenReturn(Optional.empty());

    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> certificateService.modifyCertificate(certificateDto, id));

    assertEquals("404 NOT_FOUND \"Certificate with id: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  void modifyCertificate_WorksCorrectly() {
    int id = 2;
    Certificate originalCertificate = certificates.get(id - 1);
    when(certificateRepository.findById((long) id)).thenReturn(Optional.of(originalCertificate));
    when(certificateRepository.save(any(Certificate.class))).thenReturn(originalCertificate);
    CertificateDto result = certificateService.modifyCertificate(certificateDto, (long) id);
    assertEquals(CertificateDto.convertToCertificateDto(originalCertificate),result);
  }

}
