package com.greenfoxacademy.vocseikatimasterwork.controllers;

import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.isNumeric;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNonNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.validateBindingResult;

import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.ClassRoomDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.CreateClassRoomDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.UpdateClassRoomDto;
import com.greenfoxacademy.vocseikatimasterwork.services.ClassRoomService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ohaj")
public class ClassRoomRestController {

  private final ClassRoomService roomService;

  @Autowired
  public ClassRoomRestController(ClassRoomService roomService) {
    this.roomService = roomService;
  }

  @GetMapping("/classrooms")
  @ApiOperation(value = "Lists all classrooms.")
  public ResponseEntity<List<ClassRoomDto>> listRooms() {
    return ResponseEntity.ok().body(roomService.listAllRooms());
  }

  @GetMapping("/classrooms/{id}")
  @ApiOperation(value = "Fetches and returns a room by id.")
  public ResponseEntity<ClassRoomDto> findRoomById(
      @ApiParam(value = "Id of the required room.", required = true, type = "Integer as string")
      @PathVariable String id
  ) {
    requireNonNull(id, "Id must not be null.");
    return ResponseEntity.ok().body(roomService.findRoomById(isNumeric(id)));
  }

  @PostMapping("/classrooms")
  @ApiOperation(value = "Saves a new room to the database.")
  public ResponseEntity<ClassRoomDto> addRoom(
      @ApiParam(value = "The data of the room that you want to save.")
      @RequestBody(required = false) @Valid CreateClassRoomDto newRoom,
      BindingResult bindingResult
  ) {
    requireNonNull(newRoom, "The request body is empty, you must send a valid request.");
    validateBindingResult(bindingResult);

    ClassRoomDto createdRoom = roomService.addRoom(newRoom);
    URI location = URI.create(String.format("/api/ohaj/classrooms/%s", createdRoom.getId()));
    return ResponseEntity.created(location).body(createdRoom);
  }

  @DeleteMapping("/classrooms/{id}")
  @ApiOperation(value = "Deletes a room from the database.")
  public ResponseEntity<ClassRoomDto> deleteRoomById(
      @ApiParam(value = "Id of the room that you want to delete.", required = true,
          type = "Integer as string")
      @PathVariable String id
  ) {
    requireNonNull(id, "Id must not be null.");

    return ResponseEntity.ok().body(roomService.deleteRoom(isNumeric(id)));
  }

  @PutMapping("/classrooms/{id}")
  @ApiOperation(value = "Updates an existing room in the database.")
  public ResponseEntity<CourseDto> updateRoom(
      @ApiParam(value = "Id of the room you want to update.", required = true,
          type = "Integer as string")
      @PathVariable String id,

      @ApiParam(value = "The room object that you want to update the original with.",
          required = true)
      @RequestBody(required = false) @Valid UpdateClassRoomDto room,
      BindingResult bindingResult
  ) {
    requireNonNull(id, "Id of original room is null. Please enter an id.");
    requireNonNull(room, "The request body is empty, you must send a valid request.");
    validateBindingResult(bindingResult);

    roomService.modifyRoom(room, isNumeric(id));
    return ResponseEntity.noContent().build();
  }
}
