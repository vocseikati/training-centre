package com.greenfoxacademy.vocseikatimasterwork.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.ClassRoomDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.CreateClassRoomDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos.UpdateClassRoomDto;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ClassRoomRestControllerApiTest {

  @Autowired
  private MockMvc mockMvc;

  private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype());

  private final ObjectMapper mapper = new ObjectMapper();

  private final String baseUrl = "/api/ohaj/classrooms";

  private List<ClassRoomDto> rooms;
  private CreateClassRoomDto createClassRoomDto;
  private UpdateClassRoomDto updateClassRoomDto;

  public ClassRoomRestControllerApiTest() {
    mapper.registerModule(new ParameterNamesModule());
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
  }

  @BeforeEach
  void setup() {
    rooms = Arrays.asList(
        new ClassRoomDto(1L, "theoretical room", "Lajos utca, Budapest 1033", 10,
            LocalDateTime.of(2021, 7, 24, 0, 0),
            LocalDateTime.of(2021, 7, 24, 0, 0)),
        new ClassRoomDto(2L, "practical room", "Bécsi út 96, Budapest 1035", 5,
            LocalDateTime.of(2021, 7, 20, 0, 0),
            LocalDateTime.of(2021, 7, 24, 0, 0))
    );
    createClassRoomDto = new CreateClassRoomDto();
    createClassRoomDto.setAddress("new address");
    createClassRoomDto.setCapacity(2);

    updateClassRoomDto = new UpdateClassRoomDto();
    updateClassRoomDto.setAddress("put address");
    updateClassRoomDto.setCapacity(3);
  }

  @Test
  public void list_ReturnsCorrectData() throws Exception {
    String expected = mapper.writeValueAsString(rooms);

    String actual = mockMvc.perform(get(baseUrl))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  public void findById_ReturnsCorrectData() throws Exception {
    long id = 1L;
    String expected = mapper.writeValueAsString(rooms.get((int) (id - 1)));

    String actual = mockMvc.perform(get(baseUrl + "/" + id))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  public void findById_WithNonexistentId_ReturnsNotFound() throws Exception {
    long id = 10L;
    mockMvc.perform(get(baseUrl + "/" + id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message")
            .value("404 NOT_FOUND \"Room with id: " + id + " cannot be found.\""));
  }

  @Test
  public void findByIdWithInvalidId_ReturnsBadRequest() throws Exception {
    mockMvc.perform(get(baseUrl + "/" + "id"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Id must be a number.\""));
  }

  @Test
  public void findByIdWithNegativeId_ReturnsBadRequest() throws Exception {
    long invalidId = -1;
    mockMvc.perform(get(baseUrl + "/" + invalidId))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Invalid Room Id: " + invalidId + "\""));
  }

  @Test
  @DirtiesContext
  public void create_Ok() throws Exception { //todo: a dátummal van baja!
    String content = mapper.writeValueAsString(createClassRoomDto);
    long expectedId = (long) rooms.size() + 1;
    createClassRoomDto.setId(expectedId);

    String expected = mapper.writeValueAsString(createClassRoomDto);

    String actual = mockMvc.perform(post(baseUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", baseUrl + "/" + expectedId))
        .andExpect(content().contentType(contentType))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    assertEquals(mapper.readTree(expected), mapper.readTree(actual));

    actual = mockMvc.perform(get(baseUrl + "/" + expectedId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  public void create_NullBody_ReturnsBadRequest() throws Exception {
    mockMvc.perform(post(baseUrl))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"The request body is empty, you must send a valid request.\""));
  }

  @Test
  public void create_WithIdInBody_ReturnsBadRequest() throws Exception {
    String content = mapper.writeValueAsString(rooms.get(0));
    mockMvc.perform(post(baseUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Room id must be null.\""));
  }

  @Test
  public void create_NonUniqueAddress_ReturnsBadRequest() throws Exception {
    createClassRoomDto.setAddress(rooms.get(0).getAddress());
    String content = mapper.writeValueAsString(createClassRoomDto);
    mockMvc.perform(post(baseUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value(
                "400 BAD_REQUEST \"Room address is already registered. " +
                    "Address must be unique.\""));
  }

  @Test
  public void create_InvalidField_ReturnsBadRequest() throws Exception {
    createClassRoomDto.setCapacity(null);

    String content = mapper.writeValueAsString(createClassRoomDto);
    mockMvc.perform(post(baseUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value(
                "400 BAD_REQUEST \"Invalid parameters were found: Capacity must be set.\""));
  }

  @Test
  @DirtiesContext
  public void delete_WorksCorrectly() throws Exception {
    long id = 2L;
    String expected = mapper.writeValueAsString(rooms.get((int) (id - 1)));

    String actual = mockMvc.perform(delete(baseUrl + "/" + id))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  public void delete_NonexistentId_ReturnsNotFound() throws Exception {
    long id = 100L;
    mockMvc.perform(delete(baseUrl + "/" + id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message")
            .value("404 NOT_FOUND \"Room with id: " + id + " cannot be found.\""));
  }

  @Test
  public void delete_WithBadId_ReturnsBadRequest() throws Exception {
    mockMvc.perform(delete(baseUrl + "/" + "id"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Id must be a number.\""));
  }

  @Test
  public void delete_WithNegativeId_ReturnsBadRequest() throws Exception {
    long id = -10L;
    mockMvc.perform(delete(baseUrl + "/" + id))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Invalid Room Id: " + id + "\""));
  }

  @Test
  @DirtiesContext
  public void update_WorksCorrectly() throws Exception {
    String content = mapper.writeValueAsString(updateClassRoomDto);

    long id = 2L;
    ClassRoomDto originalRoom = rooms.get((int) (id - 1));
    originalRoom.setAddress(updateClassRoomDto.getAddress());
    originalRoom.setCapacity(updateClassRoomDto.getCapacity());
    String expected = mapper.writeValueAsString(originalRoom);

    mockMvc
        .perform(put(baseUrl + "/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isNoContent());

    String actualQueried = mockMvc.perform(get(baseUrl + "/" + id))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    assertEquals(mapper.readTree(expected), mapper.readTree(actualQueried));
  }

  @Test
  public void update_NonexistentId_ReturnsNotFound() throws Exception {
    long id = 100L;
    String content = mapper.writeValueAsString(updateClassRoomDto);
    mockMvc
        .perform(put(baseUrl + "/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message")
            .value("404 NOT_FOUND \"Id of original room: " + id + " cannot be found.\""));
  }

  @Test
  public void update_WithBadId_ReturnsBadRequest() throws Exception {
    mockMvc.perform(delete(baseUrl + "/" + "id"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Id must be a number.\""));
  }

  @Test
  public void update_WithNegativeId_ReturnsBadRequest() throws Exception {
    long id = -10L;
    mockMvc.perform(delete(baseUrl + "/" + id))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Invalid Room Id: " + id + "\""));
  }

  @Test
  public void update_NullBody_ReturnsBadRequest() throws Exception {
    mockMvc.perform(put(baseUrl + "/" + 1))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"The request body is empty, you must send a valid request.\""));
  }

  @Test
  public void update_NonUniqueEmail_ReturnsBadRequest() throws Exception {
    long id = 2L;
    updateClassRoomDto.setAddress(rooms.get(0).getAddress());
    ClassRoomDto originalRoom = rooms.get((int) (id - 1));
    originalRoom.setAddress(updateClassRoomDto.getAddress());
    String content = mapper.writeValueAsString(originalRoom);

    mockMvc.perform(put(baseUrl + "/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value(
                "400 BAD_REQUEST \"Room address is already registered." +
                    " Address must be unique.\""));
  }

}
