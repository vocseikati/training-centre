package com.greenfoxacademy.vocseikatimasterwork.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.certificatesdtos.CertificateDto;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
public class CertificateRestControllerApiTest {

  @Autowired
  private MockMvc mockMvc;

  private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype());

  private final ObjectMapper mapper = new ObjectMapper();

  private final String baseUrl = "/api/ohaj/certificates";

  private List<CertificateDto> certificates;
  private CertificateDto certificateDto;

  public CertificateRestControllerApiTest() {
    mapper.registerModule(new ParameterNamesModule());
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
  }

  @BeforeEach
  void setup() {
    certificates = Arrays.asList(
        new CertificateDto(1L, "Első", "Némó", LocalDate.of(2021,6,1),
            LocalDate.of(2021,6,1),8,"exam preparation course",
            LocalDate.of(2021,7,1)),
        new CertificateDto(2L, "Második", "Miklós", LocalDate.of(2021,6,10),
            LocalDate.of(2021,6,11),16,"women basic and fashion haircuts",
            LocalDate.of(2021,7,1))
    );

    certificateDto = new CertificateDto();
    certificateDto.setCourseTitle("put title");
  }

  @Test
  public void list_ReturnsCorrectData() throws Exception {
    String expected = mapper.writeValueAsString(certificates);

    String actual = mockMvc.perform(get(baseUrl))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  public void findById_ReturnsCorrectData() throws Exception {
    long id = 1L;
    String expected = mapper.writeValueAsString(certificates.get((int) (id - 1)));

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
            .value("404 NOT_FOUND \"Certificate with id: " + id + " cannot be found.\""));
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
            .value("400 BAD_REQUEST \"Invalid Certificate Id: " + invalidId + "\""));
  }

  @Test
  @DirtiesContext
  public void delete_WorksCorrectly() throws Exception {
    long id = 2L;
    String expected = mapper.writeValueAsString(certificates.get((int) (id - 1)));

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
            .value("404 NOT_FOUND \"Certificate with id: " + id + " cannot be found.\""));
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
            .value("400 BAD_REQUEST \"Invalid Certificate Id: " + id + "\""));
  }

  @Test
  @DirtiesContext
  public void update_WorksCorrectly() throws Exception {
    String content = mapper.writeValueAsString(certificateDto);

    long id = 2L;
    CertificateDto originalCert = certificates.get((int) (id - 1));
    originalCert.setCourseTitle(certificateDto.getCourseTitle());
    String expected = mapper.writeValueAsString(originalCert);

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
    String content = mapper.writeValueAsString(certificateDto);
    mockMvc
        .perform(put(baseUrl + "/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message")
            .value("404 NOT_FOUND \"Certificate with id: " + id + " cannot be found.\""));
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
            .value("400 BAD_REQUEST \"Invalid Certificate Id: " + id + "\""));
  }

  @Test
  public void update_NullBody_ReturnsBadRequest() throws Exception {
    mockMvc.perform(put(baseUrl + "/" + 1))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Certificate must not be null.\""));
  }
}
