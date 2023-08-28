package com.greenfoxacademy.vocseikatimasterwork.services;

import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNonNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.validateLongId;

import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.EntityNotFoundException;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.certificatesdtos.CertificateDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.certificatesdtos.Certificates;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Certificate;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Student;
import com.greenfoxacademy.vocseikatimasterwork.repositories.CertificateRepository;
import com.greenfoxacademy.vocseikatimasterwork.repositories.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultCertificateService implements CertificateService {

  private final CertificateRepository certificateRepository;
  private final StudentRepository studentRepository;

  @Autowired
  public DefaultCertificateService(CertificateRepository certificateRepository,
                                   StudentRepository studentRepository) {
    this.certificateRepository = certificateRepository;
    this.studentRepository = studentRepository;
  }

  @Override
  public Certificates editCertificate(Course course) {
    requireNonNull(course, "Course must not be null.");
    if (!course.getStatus().equals(Status.FINISHED)) {
      throw new IllegalArgumentException("The course is not over yet.");
    }

    Set<Student> students = course.getStudents();
    List<Certificate> certificates = new ArrayList<>();
    for (Student student : students) {
      certificates.add(new Certificate(student.getFirstName(), student.getLastName(),
          course.getStartDate(), course.getEndDate(), course.getDurationInHours(),
          course.getTitle(), true, student));
    }
    certificateRepository.saveAll(certificates);
    Set<CertificateDto> certificateDtoList =
        certificates.stream().map(CertificateDto::convertToCertificateDto)
            .collect(Collectors.toSet());
    return new Certificates(certificateDtoList);
  }

  @Override
  public List<CertificateDto> getAllCertificates(Long studentId) {

    if (studentId == null) {
      List<Certificate> allCertificates = certificateRepository.findAll();
      return allCertificates.stream()
          .map(CertificateDto::convertToCertificateDto)
          .collect(Collectors.toList());
    }

    if (!studentRepository.existsById(studentId)) {
      throw new EntityNotFoundException("Student with id: " + studentId + " cannot be found.");
    }

    List<Certificate> allByStudentId = certificateRepository.findAllByStudentId(studentId);
    return allByStudentId.stream()
        .map(CertificateDto::convertToCertificateDto)
        .collect(Collectors.toList());
  }

  @Override
  public CertificateDto getCertificateById(Long certificateId) {
    requireNonNull(certificateId, "Please enter an id.");
    validateLongId(certificateId, "Invalid Certificate Id: " + certificateId);

    Certificate certificate = certificateRepository.findById(certificateId).orElseThrow(
        () -> new EntityNotFoundException(
            "Certificate with id: " + certificateId + " cannot be found."));

    return CertificateDto.convertToCertificateDto(certificate);
  }

  @Override
  public CertificateDto deleteCertificate(Long id) {
    requireNonNull(id, "Please enter an id.");
    validateLongId(id, "Invalid Certificate Id: " + id);

    Certificate certificate = certificateRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Certificate with id: " + id + " cannot be found."));

    certificateRepository.delete(certificate);

    return CertificateDto.convertToCertificateDto(certificate);
  }

  @Override
  public CertificateDto modifyCertificate(CertificateDto certificate, Long certificateId) {
    requireNonNull(certificate, "The request body is empty, you must send a valid request.");
    requireNonNull(certificateId, "Id of original certificate is null. Please enter an id.");
    validateLongId(certificateId, "Invalid Certificate Id: " + certificateId);

    Certificate originalCertificate = certificateRepository.findById(certificateId).orElseThrow(
        () -> new EntityNotFoundException(
            "Certificate with id: " + certificateId + " cannot be found."));

    if (certificate.getStudentFirstName() != null && !certificate.getStudentFirstName().isEmpty()) {
      originalCertificate.setStudentFirstName(certificate.getStudentFirstName());
    }
    if (certificate.getStudentLastName() != null && !certificate.getStudentLastName().isEmpty()) {
      originalCertificate.setStudentLastName(certificate.getStudentLastName());
    }
    if (certificate.getCourseStartDate() != null) {
      originalCertificate.setCourseStartDate(certificate.getCourseStartDate());
    }
    if (certificate.getCourseEndDate() != null) {
      originalCertificate.setCourseEndDate(certificate.getCourseEndDate());
    }
    if (certificate.getCourseDurationInHours() != null) {
      originalCertificate.setCourseDurationInHours(certificate.getCourseDurationInHours());
    }
    if (certificate.getCourseTitle() != null && !certificate.getCourseTitle().isEmpty()) {
      originalCertificate.setCourseTitle(certificate.getCourseTitle());
    }
    if (certificate.getCreatedAt() != null) {
      originalCertificate.setCreatedAt(certificate.getCreatedAt());
    }
    certificateRepository.save(originalCertificate);
    return CertificateDto.convertToCertificateDto(originalCertificate);
  }

}
