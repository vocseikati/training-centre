package com.greenfoxacademy.vocseikatimasterwork.repositories;

import com.greenfoxacademy.vocseikatimasterwork.models.entities.ClassRoom;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ClassRoomRepository extends CrudRepository<ClassRoom, Long> {

  List<ClassRoom> findAll();

  boolean existsClassRoomByAddress(String address);
}
