package com.greenfoxacademy.vocseikatimasterwork.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Instructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext
public class InstructorRepositoryTest {

  @Autowired
  private InstructorRepository repository;

  private final List<Instructor> expected = Arrays.asList(
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

  @Test
  public void whenFindAllCalled_ThenCorrectlyReturnsInstructorList() {
    List<Instructor> allInstructor = repository.findAll();
    assertEquals(2, allInstructor.size());
    assertEquals(expected, allInstructor);
  }

  @Test
  public void whenFindByIsCalledCorrectly_ThenReturnsCorrectly() {
    Optional<Instructor> instructorById = repository.findById(1L);
    assertTrue(instructorById.isPresent());
    assertEquals(expected.get(0), instructorById.get());
  }

  @Test
  public void whenExistsByEmailCalled_ThenCollectionProjectionWorksCorrectly() {
    assertTrue(
        repository.existsInstructorByEmail(expected.get(0).getEmail()));
    assertFalse(repository.existsInstructorByEmail("liza"));
  }
}
