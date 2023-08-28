package com.greenfoxacademy.vocseikatimasterwork.repositories;

import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.ClassRoom;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends CrudRepository<Course, Long> {

  List<Course> findAll();

  Set<Course> findAllByInstructorId(Long instructorId);

  List<Course> findAllByStatus(Status status);

  Set<Course> findAllByInstructorIdAndStatus(Long instructorId, Status status);

  @Modifying
  @Query("delete from Course c where c.classRoom=:room and c.status=:status")
  void deleteAllByClassRoom(@Param("room") ClassRoom room, @Param("status") Status status);

  @Query("select r.classRoom from Course r where(?1 between r.startDate and r.endDate) "
      + "or (?2 between r.startDate and r.endDate)")
  List<ClassRoom> notAvailableRooms(LocalDate courseStartDate, LocalDate courseEndDate);

}
