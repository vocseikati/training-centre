package com.greenfoxacademy.vocseikatimasterwork.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.EntityNotFoundException;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.ClassRoomDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.CreateClassRoomDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.UpdateClassRoomDto;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.ClassRoom;
import com.greenfoxacademy.vocseikatimasterwork.repositories.ClassRoomRepository;
import com.greenfoxacademy.vocseikatimasterwork.repositories.CourseRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultClassRoomServiceTest {

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private ClassRoomRepository roomRepository;

  @InjectMocks
  private DefaultClassRoomService roomService;

  private List<ClassRoom> rooms;

  private CreateClassRoomDto createRoomDto;
  private UpdateClassRoomDto updateRoomDto;

  @BeforeEach
  void setUp() {
    rooms = Arrays.asList(
        new ClassRoom(1L, "test name", "test address", 3,
            LocalDateTime.of(2021, 1, 1, 0, 0),
            LocalDateTime.of(2021, 1, 1, 0, 0)),
        new ClassRoom(2L, "test2 name", "test2 address", 4,
            LocalDateTime.of(2021, 1, 1, 0, 0),
            LocalDateTime.of(2021, 1, 1, 0, 0))
    );

    createRoomDto = new CreateClassRoomDto();
    createRoomDto.setName("post name");
    createRoomDto.setAddress("post address");
    createRoomDto.setCapacity(4);

    updateRoomDto = new UpdateClassRoomDto();
    updateRoomDto.setId(32L);
    updateRoomDto.setName("put name");
    updateRoomDto.setAddress("put address");
    updateRoomDto.setCapacity(3);
  }

  @Test
  public void listAllRooms_thenReturnsCorrectListOfDTOs() {
    when(roomRepository.findAll()).thenReturn(rooms);

    List<ClassRoomDto> actual = rooms.stream()
        .map(ClassRoomDto::convertToRoomDto).collect(Collectors.toList());

    List<ClassRoomDto> result = roomService.listAllRooms();

    assertEquals(2, result.size());
    assertEquals(result, actual);
  }

  @Test
  public void findRoomById_returnsDtoCorrectly() {
    int id = 1;
    when(roomRepository.findById((long) id)).thenReturn(Optional.of(rooms.get(0)));
    ClassRoomDto actual = roomService.findRoomById((long) id);
    ClassRoomDto expected = ClassRoomDto.convertToRoomDto(rooms.get(0));
    assertEquals(expected, actual);
  }

  @Test
  void findRoomByIdNonExistentId_throwsException() {
    long id = 1L;
    when(roomRepository.findById(id)).thenReturn(Optional.empty());
    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> roomService.findRoomById(id));
    assertEquals("404 NOT_FOUND \"Room with id: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  void findRoomByIdWithNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> roomService.findRoomById(null));
    assertEquals("Please enter an id.", e.getMessage());
  }

  @Test
  void findRoomByIdWithInvalidId_throwsException() {
    long id = 0L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> roomService.findRoomById(id));
    assertEquals("Invalid Room Id: " + id, e.getMessage());
  }

  @Test
  public void addRoomWithNullObject_throwsException() {
    createRoomDto = null;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> roomService.addRoom(createRoomDto));
    assertEquals("The request body is empty, you must send a valid request.", e.getMessage());
  }

  @Test
  public void addRoomWithId_throwsException() {
    createRoomDto.setId(1L);
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> roomService.addRoom(createRoomDto));
    assertEquals("Room id must be null.", e.getMessage());
  }

  @Test
  public void addRoomWithExistentAddress_throwsException() {
    String address = rooms.get(0).getAddress();
    createRoomDto.setAddress(address);
    when(roomRepository.existsClassRoomByAddress(address)).thenReturn(true);
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> roomService.addRoom(createRoomDto));
    assertEquals("Room address is already registered. Address must be unique.",
        e.getMessage());
  }

  @Test
  public void addRoom_WorksCorrectly() {
    ClassRoom roomToAdd = createRoomDto.convertToRoom();
    when(roomRepository.save(any(ClassRoom.class))).thenReturn(roomToAdd);
    when(roomRepository.existsClassRoomByAddress(createRoomDto.getAddress())).thenReturn(false);
    ClassRoomDto actual = roomService.addRoom(createRoomDto);
    assertEquals(ClassRoomDto.convertToRoomDto(roomToAdd), actual);
  }

  @Test
  public void deleteRoomByIdNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> roomService.deleteRoom(null));
    assertEquals("Please enter an id.", e.getMessage());
  }

  @Test
  public void deleteRoomByIdInvalidId_throwsException() {
    long id = 0L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> roomService.deleteRoom(id));
    assertEquals("Invalid Room Id: " + id, e.getMessage());
  }

  @Test
  public void deleteRoomNonExistentRecord_throwsException() {
    Long id = 1L;
    when(roomRepository.findById(id)).thenReturn(Optional.empty());

    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> roomService.deleteRoom(id));

    assertEquals("404 NOT_FOUND \"Room with id: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  void deleteRoom_WorksCorrectly() {
    int id = 1;
    ClassRoom roomToDelete = rooms.get(0);
    when(roomRepository.findById((long) id)).thenReturn(Optional.of(roomToDelete));
    courseRepository.deleteAllByClassRoom(roomToDelete, Status.PLANNED);
    ClassRoomDto expected = ClassRoomDto.convertToRoomDto(roomToDelete);

    ClassRoomDto actual = roomService.deleteRoom((long) id);

    assertEquals(expected, actual);
  }

  @Test
  public void modifyRoomNullBody_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> roomService.modifyRoom(null, 1L));
    assertEquals("The request body is empty, you must send a valid request.", e.getMessage());
  }

  @Test
  public void modifyRoomNullId_throwsException() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> roomService.modifyRoom(updateRoomDto, null));
    assertEquals("Id of original room is null. Please enter an id.", e.getMessage());
  }

  @Test
  public void modifyRoomInvalidId_throwsException() {
    long id = -1L;
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> roomService.modifyRoom(updateRoomDto, id));
    assertEquals("Invalid Room Id: " + id, e.getMessage());
  }

  @Test
  public void modifyRoomNonExistentId_throwsException() {
    Long id = 1L;
    when(roomRepository.findById(id)).thenReturn(Optional.empty());

    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> roomService.modifyRoom(updateRoomDto, id));

    assertEquals("404 NOT_FOUND \"Id of original room: " + id + " cannot be found.\"",
        e.getMessage());
  }

  @Test
  public void modifyRoomWithExistentAddress_throwsException() {
    int id = 2;
    when(roomRepository.findById((long) id)).thenReturn(Optional.of(rooms.get(id - 1)));
    String address = rooms.get(0).getAddress();
    updateRoomDto.setAddress(address);
    when(roomRepository.existsClassRoomByAddress(address)).thenReturn(true);
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> roomService.modifyRoom(updateRoomDto, (long) id));
    assertEquals("Room address is already registered. Address must be unique.",
        e.getMessage());
  }

  @Test
  void modifyRoom_WorksCorrectly() {
    int id = 2;
    ClassRoom originalRoom = rooms.get(id - 1);
    when(roomRepository.findById((long) id)).thenReturn(Optional.of(originalRoom));
    when(roomRepository.existsClassRoomByAddress(updateRoomDto.getAddress())).thenReturn(false);
    when(roomRepository.save(any(ClassRoom.class))).thenReturn(originalRoom);
    ClassRoomDto result = roomService.modifyRoom(updateRoomDto, (long) id);
    assertEquals(ClassRoomDto.convertToRoomDto(originalRoom),result);
  }


}
