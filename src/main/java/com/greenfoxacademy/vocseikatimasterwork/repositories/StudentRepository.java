package com.greenfoxacademy.vocseikatimasterwork.repositories;

import com.greenfoxacademy.vocseikatimasterwork.models.entities.Student;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {

  List<Student> findAll();

  boolean existsStudentByEmail(String emailAddress);
}
