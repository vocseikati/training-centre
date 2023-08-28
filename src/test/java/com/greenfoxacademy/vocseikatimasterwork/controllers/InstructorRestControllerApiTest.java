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
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.CreateInstructorDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.InstructorDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.instructordtos.UpdateInstructorDto;
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
public class InstructorRestControllerApiTest {

  @Autowired
  private MockMvc mockMvc;

  private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype());

  private final ObjectMapper mapper = new ObjectMapper();

  private final String baseUrl = "/api/ohaj/instructors";

  private List<InstructorDto> instructors;
  private CreateInstructorDto createInstructorDto;
  private UpdateInstructorDto updateInstructorDto;

  public InstructorRestControllerApiTest() {
    mapper.registerModule(new ParameterNamesModule());
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
  }

  @BeforeEach
  void setup() {

    createInstructorDto = new CreateInstructorDto();
    createInstructorDto.setFirstName("Api");
    createInstructorDto.setLastName("Tester");
    createInstructorDto.setGender(Gender.FEMALE);
    createInstructorDto.setDateOfBirth(LocalDate.of(1996, 4, 11));
    createInstructorDto.setMailingAddress("api test address");
    createInstructorDto.setBillingAddress("api test address");
    createInstructorDto.setEmail("api@test.hu");
    createInstructorDto.setPhoneNumber("123456789");

    InstructorDto instructorDto1 = new InstructorDto();
    instructorDto1.setId(1L);
    instructorDto1.setFirstName("Varga");
    instructorDto1.setLastName("Csilla");
    instructorDto1.setGender(Gender.FEMALE);
    instructorDto1.setDateOfBirth(LocalDate.of(1973, 5, 21));
    instructorDto1.setMailingAddress("Fogócska utca 1., 1113 Budapest Hungary");
    instructorDto1.setBillingAddress("Fogócska utca 1., 1113 Budapest Hungary");
    instructorDto1.setEmail("vcsilla@gmail.com");
    instructorDto1.setPhoneNumber("0036302532801");
    instructorDto1.setCreatedAt(LocalDateTime.of(2021, 7, 1, 0, 0));
    instructorDto1.setLastUpdatedAt(LocalDateTime.of(2021, 7, 17, 0, 0));

    InstructorDto instructorDto2 = new InstructorDto();
    instructorDto2.setId(2L);
    instructorDto2.setFirstName("Alma");
    instructorDto2.setLastName("Virág");
    instructorDto2.setGender(Gender.FEMALE);
    instructorDto2.setDateOfBirth(LocalDate.of(1984, 3, 25));
    instructorDto2.setMailingAddress("Etele út 54/a., 1115 Budapest Hungary");
    instructorDto2.setBillingAddress("Etele út 54/a., 1115 Budapest Hungary");
    instructorDto2.setEmail("almavirag@gmail.com");
    instructorDto2.setPhoneNumber("0036306984210");
    instructorDto2.setCreatedAt(LocalDateTime.of(2021, 7, 1, 0, 0));
    instructorDto2.setLastUpdatedAt(LocalDateTime.of(2021, 7, 17, 0, 0));

    instructors = Arrays.asList(instructorDto1, instructorDto2);

    updateInstructorDto = new UpdateInstructorDto();
  }

  @Test
  public void list_ReturnsCorrectData() throws Exception {
    String expected = mapper.writeValueAsString(instructors);

    String actual = mockMvc.perform(get(baseUrl))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  public void findById_ReturnsCorrectData() throws Exception {
    long id = 1L;
    String expected = mapper.writeValueAsString(instructors.get((int) (id - 1)));

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
            .value("404 NOT_FOUND \"Instructor with id: " + id + " cannot be found.\""));
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
            .value("400 BAD_REQUEST \"Invalid Instructor Id: " + invalidId + "\""));
  }

  @Test
  @DirtiesContext
  public void create_Ok() throws Exception { //todo: a dátummal van baja!
    String content = mapper.writeValueAsString(createInstructorDto);
    long expectedId = (long) instructors.size() + 1;
    createInstructorDto.setId(expectedId);

    String expected = mapper.writeValueAsString(createInstructorDto);

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
            .value("400 BAD_REQUEST \"Instructor must not be null.\""));
  }

  @Test
  public void create_WithIdInBody_ReturnsBadRequest() throws Exception {
    String content = mapper.writeValueAsString(instructors.get(0));
    mockMvc.perform(post(baseUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Instructor id must be null.\""));
  }

  @Test
  public void create_NonUniqueEmail_ReturnsBadRequest() throws Exception {
    createInstructorDto.setEmail(instructors.get(0).getEmail());
    String content = mapper.writeValueAsString(createInstructorDto);
    mockMvc.perform(post(baseUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value(
                "400 BAD_REQUEST \"Email address is already registered. " +
                    "Instructor email address must be unique.\""));
  }

  @Test
  public void create_InvalidField_ReturnsBadRequest() throws Exception {
    createInstructorDto.setLastName(null);

    String content = mapper.writeValueAsString(createInstructorDto);
    mockMvc.perform(post(baseUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value(
                "400 BAD_REQUEST \"Invalid parameters were found: Last name must have a value.\""));
  }

  @Test
  @DirtiesContext
  public void delete_WorksCorrectly() throws Exception {
    long id = 2L;
    String expected = mapper.writeValueAsString(instructors.get((int) (id - 1)));

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
            .value("404 NOT_FOUND \"Instructor with id: " + id + " cannot be found.\""));
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
            .value("400 BAD_REQUEST \"Invalid Instructor Id: " + id + "\""));
  }

  @Test
  @DirtiesContext
  public void update_WorksCorrectly() throws Exception {
    updateInstructorDto.setFirstName("put name");
    String content = mapper.writeValueAsString(updateInstructorDto);

    long id = 2L;
    InstructorDto originalInstructor = instructors.get((int) (id - 1));
    originalInstructor.setFirstName(updateInstructorDto.getFirstName());
    String expected = mapper.writeValueAsString(originalInstructor);

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
    String content = mapper.writeValueAsString(updateInstructorDto);
    mockMvc
        .perform(put(baseUrl + "/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message")
            .value("404 NOT_FOUND \"Instructor with id: " + id + " cannot be found.\""));
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
            .value("400 BAD_REQUEST \"Invalid Instructor Id: " + id + "\""));
  }

  @Test
  public void update_NullBody_ReturnsBadRequest() throws Exception {
    mockMvc.perform(put(baseUrl + "/" + 1))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Instructor must not be null.\""));
  }

  @Test
  public void update_NonUniqueEmail_ReturnsBadRequest() throws Exception {
    long id = 2L;
    updateInstructorDto.setEmail(instructors.get(0).getEmail());
    InstructorDto originalInstructor = instructors.get((int) (id - 1));
    originalInstructor.setEmail(updateInstructorDto.getEmail());
    String content = mapper.writeValueAsString(originalInstructor);

    mockMvc.perform(put(baseUrl + "/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value(
                "400 BAD_REQUEST \"Email address is already registered. " +
                    "Instructor email address must be unique.\""));
  }
}
