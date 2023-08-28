package com.greenfoxacademy.vocseikatimasterwork.services;

import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.ClassRoomDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.CreateClassRoomDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.UpdateClassRoomDto;
import java.util.List;

public interface ClassRoomService {

  ClassRoomDto findRoomById(Long id);

  List<ClassRoomDto> listAllRooms();

  ClassRoomDto addRoom(CreateClassRoomDto room);

  ClassRoomDto deleteRoom(Long id);

  ClassRoomDto modifyRoom(UpdateClassRoomDto room, Long idOfOriginalRoom);
}
