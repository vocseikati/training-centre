package com.greenfoxacademy.vocseikatimasterwork.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.greenfoxacademy.vocseikatimasterwork.models.entities.ClassRoom;
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
public class ClassRoomRepositoryTest {

  @Autowired
  private ClassRoomRepository repository;

  private final List<ClassRoom> expected = Arrays.asList(
      new ClassRoom(1L, "theoretical room", "Lajos utca, Budapest 1033", 10,
          LocalDateTime.of(2021, 7, 24, 0, 0),
          LocalDateTime.of(2021, 7, 24, 0, 0)),
      new ClassRoom(2L, "practical room", "Bécsi út 96, Budapest 1035", 5,
          LocalDateTime.of(2021, 7, 20, 0, 0),
          LocalDateTime.of(2021, 7, 24, 0, 0))
  );

  @Test
  public void whenFindAllCalled_ThenCorrectlyReturnsRoomList() {
    List<ClassRoom> allRoom = repository.findAll();
    assertEquals(2, allRoom.size());
    assertEquals(expected, allRoom);
  }

  @Test
  public void whenFindByIsCalledCorrectly_ThenReturnsCorrectly() {
    Optional<ClassRoom> roomById = repository.findById(1L);
    assertTrue(roomById.isPresent());
    assertEquals(expected.get(0), roomById.get());
  }

  @Test
  public void whenExistsByAddressCalled_ThenCollectionProjectionWorksCorrectly() {
    assertTrue(
        repository.existsClassRoomByAddress(expected.get(0).getAddress()));
    assertFalse(repository.existsClassRoomByAddress("liza"));
  }
}
