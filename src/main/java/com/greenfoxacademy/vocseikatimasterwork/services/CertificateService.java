package com.greenfoxacademy.vocseikatimasterwork.services;

import com.greenfoxacademy.vocseikatimasterwork.models.dtos.certificatesdtos.CertificateDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.certificatesdtos.Certificates;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import java.util.List;

public interface CertificateService {

  Certificates editCertificate(Course course);

  List<CertificateDto> getAllCertificates(Long studentId);

  CertificateDto getCertificateById(Long certificateId);

  CertificateDto deleteCertificate(Long id);

  CertificateDto modifyCertificate(CertificateDto certificate, Long certificateId);
}
