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
import com.greenfoxacademy.vocseikatimasterwork.models.CourseType;
import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos.CourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentForCourseDto;
import com.greenfoxacademy.vocseikatimasterwork.models.dtos.studentdtos.StudentsDto;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CourseManagementRestControllerApiTest {

  private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype());

  private final ObjectMapper mapper = new ObjectMapper();

  private final String baseUrl = "/api/ohaj";

  @Autowired
  private MockMvc mockMvc;

  private List<CourseDto> courses;
  private List<StudentDto> students;

  public CourseManagementRestControllerApiTest() {
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

    students = Arrays.asList(
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
  public void enrollStudentToCourse_ReturnsCorrectDataWithStudentsList() throws Exception {
    CourseDto courseToEnroll = courses.get(5);
    StudentDto student = students.get(0);
    StudentForCourseDto studentForCourseDto =
        new StudentForCourseDto(student.getId(), student.getFirstName(), student.getLastName(),
            student.getEmail(), student.getPhoneNumber(), student.getLevel());

    Set<StudentForCourseDto> expectedSet =
        new HashSet<>(Collections.singleton(studentForCourseDto));
    String expected = mapper.writeValueAsString(new StudentsDto(expectedSet));

    String actual =
        mockMvc.perform(put(baseUrl + "/registerStudentToCourse/" + courseToEnroll.getId()
            + "?studentId=" + student.getId()))
            .andExpect(status().isAccepted())
            .andExpect(content().contentType(contentType))
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  public void enrollStudentToCourse_ReturnsNotFoundWithNullStudentId() throws Exception {
    mockMvc.perform(put(baseUrl + "/registerStudentToCourse/" + courses.get(5).getId()
        + null))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Student Id must not be null.\""));
  }

  @Test
  public void removeStudentFromCourse_WorksCorrectly() throws Exception {
    CourseDto course6 = courses.get(5);
    StudentDto student1 = students.get(0);

    StudentForCourseDto studentForCourseDto =
        new StudentForCourseDto(student1.getId(), student1.getFirstName(), student1.getLastName(),
            student1.getEmail(), student1.getPhoneNumber(), student1.getLevel());

    Set<StudentForCourseDto> expectedSet =
        new HashSet<>(Collections.singleton(studentForCourseDto));
    String expected = mapper.writeValueAsString(new StudentsDto(expectedSet));

    String actual =
        mockMvc.perform(put(baseUrl + "/registerStudentToCourse/" + course6.getId()
            + "?studentId=" + student1.getId()))
            .andExpect(status().isAccepted())
            .andExpect(content().contentType(contentType))
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    assertEquals(mapper.readTree(expected), mapper.readTree(actual));

    mockMvc.perform(delete(baseUrl + "/deleteStudentFromCourse/" + course6.getId()
        + "?studentId=" + student1.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(String.valueOf(student1.getId())))
        .andExpect(status().isNoContent());

    expectedSet = new HashSet<>();
    expected = mapper.writeValueAsString(new StudentsDto(expectedSet));

    actual = mockMvc.perform(get(baseUrl + "/studentsOfCourse/" + course6.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  public void removeStudentFromCourse_ReturnsNotFoundWithNullStudentId() throws Exception {
    mockMvc.perform(delete(baseUrl + "/deleteStudentFromCourse/" + courses.get(5).getId()
        + null))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message")
            .value("400 BAD_REQUEST \"Student Id must not be null.\""));
  }
}
