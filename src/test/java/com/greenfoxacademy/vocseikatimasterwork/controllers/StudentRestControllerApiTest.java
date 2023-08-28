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
import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.CreateStudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.UpdateStudentDto;
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
public class StudentRestControllerApiTest {

  private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype());

  private final ObjectMapper mapper = new ObjectMapper();

  private CreateStudentDto newStudent;
  private UpdateStudentDto updateStudent;
  private final String baseUrl = "/api/ohaj/students";

  private List<StudentDto> dummyData;

  @Autowired
  private MockMvc mockMvc;

  public StudentRestControllerApiTest() {
    mapper.registerModule(new ParameterNamesModule());
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
  }

  @BeforeEach
  void setUp() {

    newStudent = new CreateStudentDto();
    newStudent.setFirstName("post test");
    newStudent.setLastName("post test");
    newStudent.setGender(Gender.FEMALE);
    newStudent.setDateOfBirth(LocalDate.of(2000, 1, 1));
    newStudent.setMailingAddress("post address");
    newStudent.setBillingAddress("post address");
    newStudent.setEmail("post@email");
    newStudent.setPhoneNumber("12345678");
    newStudent.setLevel("test level");

    updateStudent = new UpdateStudentDto();

    dummyData = Arrays.asList(
        new StudentDto(1L, "Első", "Némó", Gender.MALE,
            LocalDate.of(1988, 5, 21),
            "Szabadság út 93., 2040 Budaörs Hungary",
            "Petőfi Sándor utca 23., 2120 Dunakeszi Hungary",
            "harsanyiherka@freemail.hu", "0036-70-525-5200", "beginner",
            LocalDateTime.of(2021, 7, 1, 0, 0),
            LocalDateTime.of(2021, 7, 17, 0, 0)),
        new StudentDto(2L, "Második", "Miklós", Gender.MALE,
            LocalDate.of(1993, 2, 8),
            "Petőfi S. út 19., 2015 Szigetmonostor Hungary",
            "Petőfi S. út 19., 2015 Szigetmonostor Hungary",
            "vocseikati@gmail.com", "0036-20-215-8200", "beginner",
            LocalDateTime.of(2021, 7, 1, 0, 0),
            LocalDateTime.of(2021, 7, 17, 0, 0)),
        new StudentDto(3L, "Harmadik", "Mariann", Gender.FEMALE,
            LocalDate.of(1984, 3, 25),
            "Damijanich út 21., 9400 Sopron Hungary",
            "Etele út 54., 1115 Budapest Hungary",
            "mariharom@freemail.hu", "0036-30-115-3206", "restarter",
            LocalDateTime.of(2021, 7, 1, 0, 0),
            LocalDateTime.of(2021, 7, 17, 0, 0)),
        new StudentDto(4L, "Negyedik", "Júlia", Gender.FEMALE,
            LocalDate.of(1996, 9, 1),
            "Alberti Béla utca 36., 2151 Fót Hungary",
            "Alberti Béla utca 36., 2151 Fót Hungary",
            "julinegy@citromail.hu", "0036-20-899-9900", "career modifier",
            LocalDateTime.of(2021, 7, 1, 0, 0),
            LocalDateTime.of(2021, 7, 17, 0, 0)),
        new StudentDto(5L, "Ötödik", "Liza", Gender.FEMALE,
            LocalDate.of(1995, 10, 11),
            "Kiss János altábornagy utca 10., 1126 Budapest Hungary",
            "Kiss János altábornagy utca 10., 1126 Budapest Hungary",
            "lizaot@gmail.com", "0036-30-625-3206", "master",
            LocalDateTime.of(2021, 7, 1, 0, 0),
            LocalDateTime.of(2021, 7, 17, 0, 0))
    );
  }

  @Test
  public void list_ReturnsCorrectData() throws Exception {
    String expected = mapper.writeValueAsString(dummyData);

    String actual = mockMvc.perform(get(baseUrl))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  public void findById_ReturnsCorrectData() throws Exception {
    long id = 1L;
    String expected = mapper.writeValueAsString(dummyData.get((int) (id - 1)));

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
            .value("404 NOT_FOUND \"Student with id: " + id + " cannot be found.\""));
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
            .value("400 BAD_REQUEST \"Invalid Student Id: " + invalidId + "\""));
  }

  @Test
  @DirtiesContext
  public void create_Ok() throws Exception { //todo: a dátummal van baja!
    String content = mapper.writeValueAsString(newStudent);
    long expectedId = (long) dummyData.size() + 1;
    newStudent.setId(expectedId);

    String expected = mapper.writeValueAsString(newStudent);

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
            .value("400 BAD_REQUEST \"Student must not be null.\""));
  }

  @Test
  public void create_WithIdInBody_ReturnsBadRequest() throws Exception {
    String content = mapper.writeValueAsString(dummyData.get(0));
    mockMvc.perform(post(baseUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Student id must be null.\""));
  }

  @Test
  public void create_NonUniqueEmail_ReturnsBadRequest() throws Exception {
    newStudent.setEmail(dummyData.get(0).getEmail());
    String content = mapper.writeValueAsString(newStudent);
    mockMvc.perform(post(baseUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value(
                "400 BAD_REQUEST \"Email address is already registered. " +
                    "Student email address must be unique.\""));
  }

  @Test
  public void create_InvalidField_ReturnsBadRequest() throws Exception {
    newStudent.setFirstName(null);

    String content = mapper.writeValueAsString(newStudent);
    mockMvc.perform(post(baseUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value(
                "400 BAD_REQUEST \"Invalid parameters were found: First name must have a value.\""));
  }

  @Test
  @DirtiesContext
  public void delete_WorksCorrectly() throws Exception {
    long id = 2L;
    String expected = mapper.writeValueAsString(dummyData.get((int) (id - 1)));

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
            .value("404 NOT_FOUND \"Student with id: " + id + " cannot be found.\""));
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
            .value("400 BAD_REQUEST \"Invalid Student Id: " + id + "\""));
  }

  @Test
  @DirtiesContext
  public void update_WorksCorrectly() throws Exception {
    updateStudent.setFirstName("put name");
    String content = mapper.writeValueAsString(updateStudent);

    long id = 2L;
    StudentDto originalStudent = dummyData.get((int) (id - 1));
    originalStudent.setFirstName(updateStudent.getFirstName());
    String expected = mapper.writeValueAsString(originalStudent);

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
    String content = mapper.writeValueAsString(updateStudent);
    mockMvc
        .perform(put(baseUrl + "/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message")
            .value("404 NOT_FOUND \"Id of original student: " + id + " cannot be found.\""));
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
            .value("400 BAD_REQUEST \"Invalid Student Id: " + id + "\""));
  }

  @Test
  public void update_NullBody_ReturnsBadRequest() throws Exception {
    mockMvc.perform(put(baseUrl + "/" + 1))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Student must not be null.\""));
  }

  @Test
  public void update_NonUniqueEmail_ReturnsBadRequest() throws Exception {
    long id = 4L;
    updateStudent.setEmail(dummyData.get(1).getEmail());
    StudentDto originalStudent = dummyData.get((int) (id - 1));
    originalStudent.setEmail(updateStudent.getEmail());
    String content = mapper.writeValueAsString(originalStudent);

    mockMvc.perform(put(baseUrl + "/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value(
                "400 BAD_REQUEST \"Email address is already registered. " +
                    "Student email address must be unique.\""));
  }
}
