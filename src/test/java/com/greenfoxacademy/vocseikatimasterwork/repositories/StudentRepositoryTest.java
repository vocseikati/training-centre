package com.greenfoxacademy.vocseikatimasterwork.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Student;
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
public class StudentRepositoryTest {

  @Autowired
  private StudentRepository repository;

  private final List<Student> expected = Arrays.asList(
      new Student(1L, "Első", "Némó", Gender.MALE,
          LocalDate.of(1988, 5, 21),
          "Szabadság út 93., 2040 Budaörs Hungary",
          "Petőfi Sándor utca 23., 2120 Dunakeszi Hungary",
          "harsanyiherka@freemail.hu", "0036-70-525-5200", "beginner",
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0)),
      new Student(2L, "Második", "Miklós", Gender.MALE,
          LocalDate.of(1993, 2, 8),
          "Petőfi S. út 19., 2015 Szigetmonostor Hungary",
          "Petőfi S. út 19., 2015 Szigetmonostor Hungary",
          "vocseikati@gmail.com", "0036-20-215-8200", "beginner",
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0)),
      new Student(3L, "Harmadik", "Mariann", Gender.FEMALE,
          LocalDate.of(1984, 3, 25),
          "Damijanich út 21., 9400 Sopron Hungary",
          "Etele út 54., 1115 Budapest Hungary",
          "mariharom@freemail.hu", "0036-30-115-3206", "restarter",
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0)),
      new Student(4L, "Negyedik", "Júlia", Gender.FEMALE,
          LocalDate.of(1996, 9, 1),
          "Alberti Béla utca 36., 2151 Fót Hungary",
          "Alberti Béla utca 36., 2151 Fót Hungary",
          "julinegy@citromail.hu", "0036-20-899-9900", "career modifier",
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0)),
      new Student(5L, "Ötödik", "Liza", Gender.FEMALE,
          LocalDate.of(1995, 10, 11),
          "Kiss János altábornagy utca 10., 1126 Budapest Hungary",
          "Kiss János altábornagy utca 10., 1126 Budapest Hungary",
          "lizaot@gmail.com", "0036-30-625-3206", "master",
          LocalDateTime.of(2021, 7, 1, 0, 0),
          LocalDateTime.of(2021, 7, 17, 0, 0))
  );

  @Test
  public void whenFindAllCalled_ThenCorrectlyReturnsStudentList() {
    List<Student> allStudent = repository.findAll();
    assertEquals(5, allStudent.size());
    assertEquals(expected, allStudent);
  }

  @Test
  public void whenFindByIsCalledCorrectly_ThenReturnsCorrectly() {
    Optional<Student> studentById = repository.findById(1L);
    assertTrue(studentById.isPresent());
    assertEquals(expected.get(0), studentById.get());
  }

  @Test
  public void whenExistsByEmailCalled_ThenCollectionProjectionWorksCorrectly() {
    assertTrue(
        repository.existsStudentByEmail(expected.get(0).getEmail()));
    assertFalse(repository.existsStudentByEmail("liza"));
  }

}
