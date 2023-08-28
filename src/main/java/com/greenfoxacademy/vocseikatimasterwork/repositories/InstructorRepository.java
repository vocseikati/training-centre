package com.greenfoxacademy.vocseikatimasterwork.repositories;

import com.greenfoxacademy.vocseikatimasterwork.models.entities.Instructor;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface InstructorRepository extends CrudRepository<Instructor, Long> {

  List<Instructor> findAll();

  boolean existsInstructorByEmail(String emailAddress);
}
