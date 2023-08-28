package com.greenfoxacademy.vocseikatimasterwork.repositories;

import com.greenfoxacademy.vocseikatimasterwork.models.entities.Certificate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CertificateRepository extends CrudRepository<Certificate, Long> {

  List<Certificate> findAll();

  List<Certificate> findAllByStudentId(Long studentId);
}
