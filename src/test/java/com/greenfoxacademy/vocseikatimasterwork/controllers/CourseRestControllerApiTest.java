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
import com.greenfoxacademy.vocseikatimasterwork.models.CourseType;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CreateCourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.UpdateCourseDto;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
public class CourseRestControllerApiTest {

  private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype());

  private final ObjectMapper mapper = new ObjectMapper();

  private List<CourseDto> courses;
  private CreateCourseDto createCourseDto;
  private UpdateCourseDto updateCourseDto;

  private final String baseUrl = "/api/ohaj/courses";

  @Autowired
  private MockMvc mockMvc;

  public CourseRestControllerApiTest() {
    mapper.registerModule(new ParameterNamesModule());
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
  }

  @BeforeEach
  void setUp() {
    courses = Arrays.asList(
        new CourseDto(1L, "exam preparation course",
            LocalDate.of(2021, 6, 1),
            LocalDate.of(2021, 6, 1), 8, true,
            40000.0, CourseType.THEORETICAL, Status.FINISHED, 1L, 1L,
            LocalDateTime.of(2021, 7, 1, 0, 0),
            LocalDateTime.of(2021, 7, 17, 0, 0)),
        new CourseDto(2L, "women basic and fashion haircuts",
            LocalDate.of(2021, 6, 10),
            LocalDate.of(2021, 6, 11), 16, false,
            30000.0, CourseType.PRACTICAL, Status.FINISHED, 2L, 2L,
            LocalDateTime.of(2021, 7, 1, 0, 0),
            LocalDateTime.of(2021, 7, 17, 0, 0)),
        new CourseDto(3L, "men fashion and classic haircut technologies",
            LocalDate.of(2021, 6, 1),
            LocalDate.of(2021, 6, 9), 40, false,
            32000.0, CourseType.PRACTICAL, Status.FINISHED, 1L, 2L,
            LocalDateTime.of(2021, 7, 1, 0, 0),
            LocalDateTime.of(2021, 7, 17, 0, 0)),
        new CourseDto(4L, "haircut techniques",
            LocalDate.of(2021, 6, 20),
            LocalDate.of(2021, 6, 20), 8, true,
            40000.0, CourseType.THEORETICAL, Status.FINISHED, 1L, 1L,
            LocalDateTime.of(2021, 7, 1, 0, 0),
            LocalDateTime.of(2021, 7, 17, 0, 0)),
        new CourseDto(5L, "hair drying for women",
            LocalDate.of(2021, 7, 21),
            LocalDate.of(2021, 7, 22), 15, false,
            30000.0, CourseType.PRACTICAL, Status.FINISHED, 2L, 2L,
            LocalDateTime.of(2021, 7, 1, 0, 0),
            LocalDateTime.of(2021, 7, 17, 0, 0)),
        new CourseDto(6L, "painting techniques",
            LocalDate.of(2021, 8, 1),
            LocalDate.of(2021, 9, 15), 60, false,
            35000.0, CourseType.PRACTICAL, Status.IN_PROGRESS, 2L, 2L,
            LocalDateTime.of(2021, 7, 1, 0, 0),
            LocalDateTime.of(2021, 7, 17, 0, 0)),
        new CourseDto(7L, "haircuts with razors and trimmers",
            LocalDate.of(2021, 9, 18),
            LocalDate.of(2021, 9, 18), 8, false,
            30000.0, CourseType.PRACTICAL, Status.PLANNED, 1L, 1L,
            LocalDateTime.of(2021, 7, 1, 0, 0),
            LocalDateTime.of(2021, 7, 17, 0, 0))
    );

    createCourseDto = new CreateCourseDto();
    createCourseDto.setTitle("post title");
    createCourseDto.setStartDate(LocalDate.of(2021,10,1));
    createCourseDto.setEndDate(LocalDate.of(2021,10,10));
    createCourseDto.setDurationInHours(40);
    createCourseDto.setIndividualEducation(false);
    createCourseDto.setPrice(70000.0);
    createCourseDto.setClassRoomId(1L);

    updateCourseDto = new UpdateCourseDto();
    updateCourseDto.setTitle("put title");
    updateCourseDto.setStartDate(LocalDate.of(2021,3,25));
    updateCourseDto.setEndDate(LocalDate.of(2021,3,25));
    updateCourseDto.setDurationInHours(40);
    updateCourseDto.setIndividualEducation(false);
    updateCourseDto.setPrice(70000.0);
    updateCourseDto.setClassRoomId(1L);
  }

  @Test
  public void list_ReturnsCorrectData() throws Exception {
    String expected = mapper.writeValueAsString(courses);

    String actual = mockMvc.perform(get(baseUrl))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  public void findById_ReturnsCorrectData() throws Exception {
    long id = 1L;
    String expected = mapper.writeValueAsString(courses.get((int) (id - 1)));

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
            .value("404 NOT_FOUND \"Course with id: " + id + " cannot be found.\""));
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
            .value("400 BAD_REQUEST \"Invalid Course Id: " + invalidId + "\""));
  }

  @Test
  @DirtiesContext
  public void create_Ok() throws Exception {
    String content = mapper.writeValueAsString(createCourseDto);
    long expectedId = (long) courses.size() + 1;
    createCourseDto.setId(expectedId);

    String expected = mapper.writeValueAsString(createCourseDto);

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
            .value("400 BAD_REQUEST \"Course must not be null.\""));
  }

  @Test
  public void create_WithIdInBody_ReturnsBadRequest() throws Exception {
    String content = mapper.writeValueAsString(courses.get(6));
    mockMvc.perform(post(baseUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Course id must be null.\""));
  }

  @Test
  public void create_InvalidField_ReturnsBadRequest() throws Exception {
    createCourseDto.setTitle(null);

    String content = mapper.writeValueAsString(createCourseDto);
    mockMvc.perform(post(baseUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value(
                "400 BAD_REQUEST \"Invalid parameters were found: Title must have a value.\""));
  }

  @Test
  @DirtiesContext
  public void delete_WorksCorrectly() throws Exception {
    long id = 2L;
    String expected = mapper.writeValueAsString(courses.get((int) (id - 1)));

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
            .value("404 NOT_FOUND \"Course with id: " + id + " cannot be found.\""));
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
            .value("400 BAD_REQUEST \"Invalid Course Id: " + id + "\""));
  }

  @Test
  @DirtiesContext
  public void update_WorksCorrectly() throws Exception {
    String content = mapper.writeValueAsString(updateCourseDto);

    long id = 2L;
    CourseDto originalCourse = courses.get((int) (id - 1));
    originalCourse.setTitle(updateCourseDto.getTitle());
    originalCourse.setStartDate(updateCourseDto.getStartDate());
    originalCourse.setEndDate(updateCourseDto.getEndDate());
    originalCourse.setDurationInHours(updateCourseDto.getDurationInHours());
    originalCourse.setIndividualEducation(updateCourseDto.getIndividualEducation());
    originalCourse.setPrice(updateCourseDto.getPrice());

    String expected = mapper.writeValueAsString(originalCourse);

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
    String content = mapper.writeValueAsString(updateCourseDto);
    mockMvc
        .perform(put(baseUrl + "/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message")
            .value("404 NOT_FOUND \"Id of original course: " + id + " cannot be found.\""));
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
            .value("400 BAD_REQUEST \"Invalid Course Id: " + id + "\""));
  }

  @Test
  public void update_NullBody_ReturnsBadRequest() throws Exception {
    mockMvc.perform(put(baseUrl + "/" + 1))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Course must not be null.\""));
  }

}
