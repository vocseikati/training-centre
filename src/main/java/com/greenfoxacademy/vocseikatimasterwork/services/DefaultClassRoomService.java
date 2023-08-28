package com.greenfoxacademy.vocseikatimasterwork.services;

import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNonNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.validateLongId;

import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.EntityNotFoundException;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.ClassRoomDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.CreateClassRoomDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.UpdateClassRoomDto;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.ClassRoom;
import com.greenfoxacademy.vocseikatimasterwork.repositories.ClassRoomRepository;
import com.greenfoxacademy.vocseikatimasterwork.repositories.CourseRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultClassRoomService implements ClassRoomService {

  private final ClassRoomRepository roomRepository;
  private final CourseRepository courseRepository;

  @Autowired
  public DefaultClassRoomService(ClassRoomRepository roomRepository,
                                 CourseRepository courseRepository) {
    this.roomRepository = roomRepository;
    this.courseRepository = courseRepository;
  }

  @Override
  public ClassRoomDto findRoomById(Long id) {
    requireNonNull(id, "Please enter an id.");
    validateLongId(id, "Invalid Room Id: " + id);

    ClassRoom classRoom = roomRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Room with id: " + id + " cannot be found."));

    return ClassRoomDto.convertToRoomDto(classRoom);
  }

  @Override
  public List<ClassRoomDto> listAllRooms() {
    return roomRepository.findAll().stream()
        .map(ClassRoomDto::convertToRoomDto)
        .collect(Collectors.toList());
  }

  @Override
  public ClassRoomDto addRoom(CreateClassRoomDto room) {
    requireNonNull(room, "The request body is empty, you must send a valid request.");
    requireNull(room.getId(), "Room id must be null.");

    if (roomRepository.existsClassRoomByAddress(room.getAddress())) {
      throw new IllegalArgumentException(
          "Room address is already registered. Address must be unique.");
    }

    ClassRoom roomSaved = roomRepository.save(room.convertToRoom());
    return ClassRoomDto.convertToRoomDto(roomSaved);
  }

  @Override
  public ClassRoomDto deleteRoom(Long id) {
    requireNonNull(id, "Please enter an id.");
    validateLongId(id, "Invalid Room Id: " + id);

    ClassRoom classRoom = roomRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Room with id: " + id + " cannot be found."));

    courseRepository.deleteAllByClassRoom(classRoom, Status.PLANNED);
    roomRepository.delete(classRoom);

    return ClassRoomDto.convertToRoomDto(classRoom);
  }

  @Override
  public ClassRoomDto modifyRoom(UpdateClassRoomDto room, Long idOfOriginalRoom) {
    requireNonNull(room, "The request body is empty, you must send a valid request.");
    requireNonNull(idOfOriginalRoom, "Id of original room is null. Please enter an id.");
    validateLongId(idOfOriginalRoom, "Invalid Room Id: " + idOfOriginalRoom);

    ClassRoom originalRoom = roomRepository.findById(idOfOriginalRoom).orElseThrow(
        () -> new EntityNotFoundException(
            "Id of original room: " + idOfOriginalRoom + " cannot be found."));

    if (!originalRoom.getAddress().equals(room.getAddress()) && room.getAddress() != null
        && !room.getAddress().isEmpty()) {
      if (roomRepository.existsClassRoomByAddress(room.getAddress())) {
        throw new IllegalArgumentException(
            "Room address is already registered. Address must be unique.");
      }
      originalRoom.setAddress(room.getAddress());
    }

    if (room.getName() != null && !room.getName().isEmpty()) {
      originalRoom.setName(room.getName());
    }
    if (room.getCapacity() != null) {
      originalRoom.setCapacity(room.getCapacity());
    }

    roomRepository.save(originalRoom);
    return ClassRoomDto.convertToRoomDto(originalRoom);
  }
}
